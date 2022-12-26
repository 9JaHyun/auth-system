package com.authserver.adapter.out.redis;

import com.authserver.adapter.out.jwt.JwtToken;
import com.authserver.application.port.out.redis.JwtRedisCommandPort;
import com.authserver.application.port.out.redis.JwtRedisQueryPort;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
class JwtRedisAdapter implements JwtRedisQueryPort, JwtRedisCommandPort {

    private final RedisService redisService;

    public JwtRedisAdapter(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void saveRefreshToken(JwtToken jwtToken) {
        redisService.set("refreshToken-" + jwtToken.username(), jwtToken.token(),
            Duration.of(jwtToken.expiredTime(), ChronoUnit.MILLIS));
    }

    @Override
    public String getRefreshToken(String username) {
        return redisService.get("refreshToken-" + username, String.class)
            .orElseThrow(() -> new AssertionError("no RefreshToken in Redis"));
    }
}
