package com.authserver.application.port.out.redis;

public interface JwtRedisQueryPort {

    String getRefreshToken(String username);
}
