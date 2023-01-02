package com.userservice.application.port.in;

public interface  EmailUseCase {

    void sendAuthEmail(String username, String email);
}
