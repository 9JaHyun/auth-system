package com.userservice.adapter.in.api.dto;

import com.userservice.domain.User;

public record UserResponse(
    String username,
    String nickname,
    String email
) {

    public UserResponse(User user) {
        this(user.username(), user.nickname(),
            user.email().replaceAll("(?<=.{2}).(?=.*@)", "*"));
    }
}
