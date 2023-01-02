package com.authserver.adapter.in.web;

public record SignInRequest(
    String username,
    String password
) {
}
