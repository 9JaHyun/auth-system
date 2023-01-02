package com.authserver.adapter.out.redis;

import com.authserver.application.port.out.redis.UserRedisCommandPort;
import com.authserver.application.port.out.redis.UserRedisQueryPort;
import com.authserver.domain.User;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRedisAdapter implements UserRedisCommandPort, UserRedisQueryPort {

    private final RedisService redisService;
    private final Long recentLoginMaxAge;

    public UserRedisAdapter(RedisService redisService, @Value("${redis-maxAge.recent-login}") Long recentLoginMaxAge) {
        this.redisService = redisService;
        this.recentLoginMaxAge = recentLoginMaxAge;
    }

    @Override
    public void saveRecentSignInUser(User user) {
        redisService.set("loginUser-" + user.username(), user, Duration.of(recentLoginMaxAge, ChronoUnit.MILLIS));
    }

    @Override
    public User getCachedUser(String username) {
        return redisService.get("loginUser-" + username, User.class)
            .orElseThrow(() -> new AssertionError("User not cached in Redis"));
    }
}
