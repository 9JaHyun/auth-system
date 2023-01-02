package com.authserver.adapter.out.jwt;

public record JwtToken(
    String username,
    String token,
    long expiredTime,
    JwtTokenType jwtTokenType
) {

    public enum JwtTokenType {
        ACCESS,
        REFRESH,
        BLACKLIST
    }

}
