package com.authserver.application.service;

import com.authserver.adapter.out.jwt.JwtToken;
import com.authserver.adapter.out.jwt.JwtToken.JwtTokenType;
import com.authserver.application.port.in.SignInRequestDto;
import com.authserver.application.port.in.SignInResponseDto;
import com.authserver.application.port.in.SignInUseCase;
import com.authserver.application.port.out.jwt.JwtPort;
import com.authserver.application.port.out.persistence.UserPersistenceQueryPort;
import com.authserver.application.port.out.redis.JwtRedisCommandPort;
import com.authserver.application.port.out.redis.UserRedisCommandPort;
import com.authserver.application.port.out.redis.UserRedisQueryPort;
import com.authserver.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SignInService implements SignInUseCase {

    private final UserPersistenceQueryPort userPersistenceQueryPort;
    private final UserRedisCommandPort userRedisCommandPort;
    private final UserRedisQueryPort userRedisQueryPort;
    private final JwtPort jwtPort;
    private final JwtRedisCommandPort jwtRedisCommandPort;

    public SignInService(UserPersistenceQueryPort userPersistenceQueryPort, UserRedisCommandPort userRedisCommandPort,
        UserRedisQueryPort userRedisQueryPort, JwtPort jwtPort,
        JwtRedisCommandPort jwtRedisCommandPort) {
        this.userPersistenceQueryPort = userPersistenceQueryPort;
        this.userRedisCommandPort = userRedisCommandPort;
        this.userRedisQueryPort = userRedisQueryPort;
        this.jwtPort = jwtPort;
        this.jwtRedisCommandPort = jwtRedisCommandPort;
    }

    @Override
    public SignInResponseDto signIn(SignInRequestDto requestDto) {
        User user = getUser(requestDto);
        if (!user.password().equals(requestDto.password())) {
            throw new AssertionError("로그인 정보 불일치");
        }
        JwtToken access = jwtPort.create(requestDto.username(), JwtTokenType.ACCESS);
        JwtToken refresh = jwtPort.create(requestDto.username(), JwtTokenType.REFRESH);
        jwtRedisCommandPort.saveRefreshToken(refresh);
        return new SignInResponseDto(access.token(), "bearer", refresh.token());
    }

    private User getUser(SignInRequestDto requestDto) {
        User user;
        try {
            user = userRedisQueryPort.getCachedUser(requestDto.username());
        } catch (AssertionError e) {
            user = userPersistenceQueryPort.getUser(requestDto.username());
            cacheUser(user);
        } catch (Exception e) {
            log.info("No User..");
            throw e;
        }
        return user;
    }

    private void cacheUser(User user) {
        userRedisCommandPort.saveRecentSignInUser(user);
    }
}
