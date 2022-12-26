package com.userservice.application.service;

import com.userservice.application.port.in.EmailUseCase;
import com.userservice.application.port.out.email.EmailPort;
import com.userservice.application.port.out.email.emailStrategy.AuthSendingEmailStrategy;
import com.userservice.application.port.out.persistence.UserQueryPort;
import com.userservice.application.port.out.redis.UserRedisCommandPort;
import com.userservice.domain.User;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailUseCase {

    private final UserQueryPort userQueryPort;
    private final UserRedisCommandPort userRedisCommandPort;
    private final EmailPort emailPort;

    public EmailService(UserQueryPort userQueryPort, UserRedisCommandPort userRedisCommandPort,
        EmailPort emailPort) {
        this.userQueryPort = userQueryPort;
        this.userRedisCommandPort = userRedisCommandPort;
        this.emailPort = emailPort;
    }

    @Override
    public void sendAuthEmail(String username, String email) {
        User user = userQueryPort.getByUsername(username);

        if (!user.email().equals(email)) {
            throw new AssertionError("Invalid user info");
        }

        String authCode = UUID.randomUUID().toString();
        userRedisCommandPort.saveAuthCode(username, authCode);
        emailPort.sendEmail(user.email(),
            new AuthSendingEmailStrategy("localhost:6666/user-service/email", username, authCode));
    }
}
