package com.userservice.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.userservice.application.port.out.email.EmailPort;
import com.userservice.application.port.out.email.emailStrategy.SendingEmailStrategy;
import com.userservice.application.port.out.persistence.UserQueryPort;
import com.userservice.application.port.out.redis.UserRedisCommandPort;
import com.userservice.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailServiceTest {
    private final UserQueryPort userQueryPort = Mockito.mock(UserQueryPort.class);
    private final UserRedisCommandPort userRedisCommandPort = Mockito.mock(UserRedisCommandPort.class);
    private final EmailPort emailPort = Mockito.mock(EmailPort.class);
    private final EmailService emailService = new EmailService(userQueryPort, userRedisCommandPort,
        emailPort);

    @DisplayName("인증 메일 전송 테스트 시")
    @Nested
    class Case_SendAuthEmail {

        @DisplayName("회원 정보가 없는 경우 실패")
        @Test
        void fail_userNotExist(){
            // given
            User user = givenAnUser("username", "email.gmail.com");
            givenUserWillFail_NotExist(user);

            // when then
            Assertions.assertThatThrownBy(
                    () -> emailService.sendAuthEmail(user.username(), user.email()))
                .isInstanceOf(AssertionError.class)
                .hasMessage("no user data exist");
            thenShouldNotInvokeCachingAuthInfo();
            thenShouldNotInvokeSendingAuthEmail();
        }

        @DisplayName("요청 정보가 일치하지 않는 경우 실패")
        @Test
        void fail_requestInfoNotMatch(){
            // given
            User user = givenAnUser("username", "email.gmail.com");
            givenUserWillFail_NotMatchEmail(user);

            // when then
            Assertions.assertThatThrownBy(
                    () -> emailService.sendAuthEmail(user.username(), user.email()))
                .isInstanceOf(AssertionError.class)
                .hasMessage("Invalid user info");
            thenShouldNotInvokeCachingAuthInfo();
            thenShouldNotInvokeSendingAuthEmail();
        }

        @Test
        void success_sendAuthEmail(){
            // given
            User user = givenAnUser("username", "email@gmail.com");
            givenUserWillSuccess(user);

            // when
            emailService.sendAuthEmail(user.username(), user.email());

            // then
            then(userRedisCommandPort).should(times(1))
                .saveAuthCode(anyString(), anyString());
            then(emailPort)
                .should(times(1))
                .sendEmail(anyString(), any(SendingEmailStrategy.class));
        }

        private void givenUserWillSuccess(User user) {
            given(userQueryPort.getByUsername(eq(user.username())))
                .willReturn(user);
        }

        private void givenUserWillFail_NotExist(User user) {
            given(userQueryPort.getByUsername(eq(user.username())))
                .willThrow(new AssertionError("no user data exist"));
        }

        private void givenUserWillFail_NotMatchEmail(User user) {
            User notMatchedUser = Mockito.mock(User.class);
            given(notMatchedUser.email()).willReturn("일치하지 않는 Email");
            given(userQueryPort.getByUsername(eq(user.username())))
                .willReturn(notMatchedUser);
        }

        private void thenShouldNotInvokeCachingAuthInfo() {
            then(userRedisCommandPort)
                .should(times(0))
                .saveAuthCode(anyString(), anyString());
        }

        private void thenShouldNotInvokeSendingAuthEmail() {
            then(emailPort)
                .should(times(0))
                .sendEmail(anyString(), any(SendingEmailStrategy.class));
        }

    }

    private User givenAnUser(String username, String email) {
        User user = Mockito.mock(User.class);
        given(user.username()).willReturn(username);
        given(user.email()).willReturn(email);
        return user;
    }
}