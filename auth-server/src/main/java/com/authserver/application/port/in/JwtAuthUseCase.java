package com.authserver.application.port.in;

public interface JwtAuthUseCase {

    String create(String username);
    boolean verify(String token);
}
