package com.userservice.adapter.in.api.dto;

import com.userservice.application.port.in.dto.SignUpRequestDto;

public class UserDtoMapper {

    public SignUpRequestDto toServiceDto(SignUpRequest request) {
        return new SignUpRequestDto(request.username(), request.password(), request.nickname(), request.email());
    }
}
