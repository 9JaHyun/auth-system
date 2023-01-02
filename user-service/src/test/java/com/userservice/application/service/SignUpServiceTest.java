package com.userservice.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.userservice.application.port.in.dto.SignUpRequestDto;
import com.userservice.application.port.out.persistence.UserCommandPort;
import com.userservice.application.port.out.persistence.UserQueryPort;
import com.userservice.application.port.out.redis.UserRedisQueryPort;
import com.userservice.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class SignUpServiceTest {

    private final UserCommandPort userCommandPort = Mockito.mock(UserCommandPort.class);
    private final UserQueryPort userQueryPort = Mockito.mock(UserQueryPort.class);
    private final UserRedisQueryPort userRedisQueryPort = Mockito.mock(UserRedisQueryPort.class);
    private final SignUpService signUpService =
        new SignUpService(userCommandPort, userQueryPort, userRedisQueryPort);

    @DisplayName("회원 가입 테스트 시")
    @Nested
    class Case_SignUp {

        @DisplayName("아이디가 중복되는 경우 실패")
        @Test
        void fail_duplicateUsername() {
            // given
            SignUpRequestDto requestDto = givenAnSignUpRequestDto("username1", "nickname1", "email1@gmail.com");
            duplicateUsernameWillFail(requestDto);

            // when then
            assertThatThrownBy(() -> signUpService.signUp(requestDto))
                .isInstanceOf(AssertionError.class)
                .hasMessage("duplicate username");
        }

        @DisplayName("닉네임이 중복되는 경우 실패")
        @Test
        void fail_duplicateNickname() {
            // given
            SignUpRequestDto requestDto = givenAnSignUpRequestDto("username1", "nickname1", "email1@gmail.com");
            duplicateNicknameWillFail(requestDto);

            // when then
            assertThatThrownBy(() -> signUpService.signUp(requestDto))
                .isInstanceOf(AssertionError.class)
                .hasMessage("duplicate nickname");
        }

        @DisplayName("이메일이 중복되는 경우 실패")
        @Test
        void fail_duplicateEmail() {
            // given
            SignUpRequestDto requestDto = givenAnSignUpRequestDto("username1", "nickname1", "email1@gmail.com");
            duplicateEmailWillFail(requestDto);

            // when then
            assertThatThrownBy(() -> signUpService.signUp(requestDto))
                .isInstanceOf(AssertionError.class)
                .hasMessage("duplicate email");
        }

        @DisplayName("중복이 없는 경우 성공")
        @Test
        void success_signUp(){
            // given
            SignUpRequestDto requestDto = givenAnSignUpRequestDto("username", "nickname",
                "email");

            // when
            signUpService.signUp(requestDto);

            // then
            thenUserCreated(requestDto);
        }

        private void duplicateUsernameWillFail(SignUpRequestDto requestDto) {
            given(userQueryPort.isExistByUsername(requestDto.username())).willReturn(true);
        }

        private void duplicateNicknameWillFail(SignUpRequestDto requestDto) {
            given(userQueryPort.isExistByNickname(requestDto.nickname())).willReturn(true);
        }

        private void duplicateEmailWillFail(SignUpRequestDto requestDto) {
            given(userQueryPort.isExistByEmail(requestDto.email())).willReturn(true);
        }
    }

    private SignUpRequestDto givenAnSignUpRequestDto(String username, String nickname,
        String email) {
        SignUpRequestDto requestDto = Mockito.mock(SignUpRequestDto.class);
        given(requestDto.username()).willReturn(username);
        given(requestDto.nickname()).willReturn(nickname);
        given(requestDto.email()).willReturn(email);
        return requestDto;
    }

    private void thenUserCreated(SignUpRequestDto... dtos) {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        then(userCommandPort).should(times(dtos.length))
            .save(userCaptor.capture());

        List<String> createdUsernames = userCaptor.getAllValues()
            .stream()
            .map(User::username)
            .collect(Collectors.toList());

        for(SignUpRequestDto dto : dtos){
            assertThat(createdUsernames).contains(dto.username());
        }
    }
}