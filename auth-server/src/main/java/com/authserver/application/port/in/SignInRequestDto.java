package com.authserver.application.port.in;

import com.authserver.adapter.in.web.SignInRequest;

public record SignInRequestDto(
    String username,
    String password
) {

    public static SignInRequestDto of(SignInRequest request) {
        return new SignInRequestDto(request.username(), request.password());
    }
}