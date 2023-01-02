package com.userservice.application.port.in.dto;

public record SignInRequestDto(
    String username,
    String password
) {

}
