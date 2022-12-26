package com.authserver.application.port.in;

import org.springframework.stereotype.Component;

@Component
public interface SignInUseCase {
    SignInResponseDto signIn(SignInRequestDto requestDto);
}
