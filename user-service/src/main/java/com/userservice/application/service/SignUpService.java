package com.userservice.application.service;

import com.userservice.adapter.in.api.dto.UserResponse;
import com.userservice.application.port.in.SignUpUseCase;
import com.userservice.application.port.in.dto.EmailAuthRequestDto;
import com.userservice.application.port.in.dto.SignUpRequestDto;
import com.userservice.application.port.out.persistence.UserCommandPort;
import com.userservice.application.port.out.persistence.UserQueryPort;
import com.userservice.application.port.out.redis.UserRedisQueryPort;
import com.userservice.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SignUpService implements SignUpUseCase {

    private final UserCommandPort userCommandPort;
    private final UserQueryPort userQueryPort;
    private final UserRedisQueryPort userRedisQueryPort;

    public SignUpService(UserCommandPort userCommandPort, UserQueryPort userQueryPort,
        UserRedisQueryPort userRedisQueryPort) {
        this.userCommandPort = userCommandPort;
        this.userQueryPort = userQueryPort;
        this.userRedisQueryPort = userRedisQueryPort;
    }

    @Override
    public UserResponse signUp(SignUpRequestDto requestDto) {
        checkDuplicate(requestDto);

        User user = User.createUser(requestDto.username(), requestDto.password(),
            requestDto.nickname(), requestDto.email());
        userCommandPort.save(user);

        return new UserResponse(user);
    }

    private void checkDuplicate(SignUpRequestDto requestDto) {
        if (userQueryPort.isExistByUsername(requestDto.username())) {
            throw new AssertionError("duplicate username");
        }
        if (userQueryPort.isExistByNickname(requestDto.nickname())) {
            throw new AssertionError("duplicate nickname");
        }
        if (userQueryPort.isExistByEmail(requestDto.email())) {
            throw new AssertionError("duplicate email");
        }
    }

    @Override
    public void checkAuthCode(EmailAuthRequestDto dto) {
        String authCode = userRedisQueryPort.getAuthCode(dto.username());

        if (!dto.authCode().equals(authCode)) {
            throw new AssertionError("잘못된 인증코드입니다.");
        }

        User user = userQueryPort.getByUsername(dto.username());
        User updatedUser = user.activate();

        userCommandPort.update(updatedUser);
    }
}
