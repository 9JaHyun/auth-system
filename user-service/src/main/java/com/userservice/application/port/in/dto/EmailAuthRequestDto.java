package com.userservice.application.port.in.dto;

public record EmailAuthRequestDto(
    String username,
    String email,
    String authCode
) {

}
