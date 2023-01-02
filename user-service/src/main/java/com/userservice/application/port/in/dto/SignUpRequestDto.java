package com.userservice.application.port.in.dto;

public record SignUpRequestDto(
    String username,
    String password,
    String nickname,
    String email
) {

}
