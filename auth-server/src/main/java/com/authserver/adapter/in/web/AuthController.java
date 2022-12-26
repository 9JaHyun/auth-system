package com.authserver.adapter.in.web;

import com.authserver.application.port.in.SignInRequestDto;
import com.authserver.application.port.in.SignInResponseDto;
import com.authserver.application.port.in.SignInUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AuthController {

    private final SignInUseCase signInUseCase;

    public AuthController(SignInUseCase signInUseCase) {
        this.signInUseCase = signInUseCase;
    }

    @GetMapping("/signIn")
    public ResponseEntity<SignInResponseDto> auth(SignInRequest request) {
        SignInResponseDto signInResponseDto = signInUseCase.signIn(SignInRequestDto.of(request));
        return ResponseEntity.ok(signInResponseDto);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK!");
    }
}
