package com.authserver.adapter.out.jwt;

public record JwtVerifyResult(
    String username,
    TokenStatus tokenStatus
) {

    public enum TokenStatus {
        AVAILABLE,
        EXPIRED,
        DENIED
    }
}
