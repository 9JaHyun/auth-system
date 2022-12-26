package com.userservice.adapter.in.api.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record SignUpRequest(
    @NotBlank String username,
    @NotBlank String password,
    @NotBlank String nickname,
    @NotBlank @Email String email
) {

}
