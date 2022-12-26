package com.authserver.application.port.out.redis;

import com.authserver.adapter.out.jwt.JwtToken;

public interface JwtRedisCommandPort {

    void saveRefreshToken(JwtToken jwtToken);
}
