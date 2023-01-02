package com.userservice.adapter.out.redis;

import com.userservice.application.port.out.redis.UserRedisCommandPort;
import com.userservice.application.port.out.redis.UserRedisQueryPort;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRedisAdapter implements UserRedisCommandPort, UserRedisQueryPort {

    private final RedisService redisService;

    @Value("${redis-maxAge.auth-code}")
    private Long authCodeMaxAge;

    public UserRedisAdapter(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void saveAuthCode(String username, String authCode) {
        redisService.set("authCode-"+ username, authCode, Duration.of(authCodeMaxAge, ChronoUnit.MILLIS));
    }

    @Override
    public String getAuthCode(String username) {
        return redisService.get("authCode-" + username, String.class).orElseThrow();
    }
}
