package com.authserver.application.port.in;

public record SignInResponseDto(
    String access_token,
    String token_type,
    String refresh_token
) {
}
