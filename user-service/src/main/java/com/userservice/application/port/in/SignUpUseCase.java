package com.userservice.application.port.in;

import com.userservice.adapter.in.api.dto.UserResponse;
import com.userservice.application.port.in.dto.EmailAuthRequestDto;
import com.userservice.application.port.in.dto.SignUpRequestDto;

public interface SignUpUseCase {

    UserResponse signUp(SignUpRequestDto requestDto);

    void checkAuthCode(EmailAuthRequestDto dto);
}
