package com.userservice.adapter.in.api.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record SendAuthMailRequest(
    @NotBlank String username,
    @Email String email
) {

}
