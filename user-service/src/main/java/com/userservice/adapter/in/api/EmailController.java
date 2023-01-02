package com.userservice.adapter.in.api;

import com.userservice.adapter.in.api.dto.SendAuthMailRequest;
import com.userservice.application.port.in.EmailUseCase;
import com.userservice.filter.RateLimit;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailUseCase emailUseCase;

    public EmailController(EmailUseCase emailUseCase) {
        this.emailUseCase = emailUseCase;
    }

    @RateLimit
    @PostMapping("/authEmail")
    public void sendAuthMail(@Valid SendAuthMailRequest request) {
        emailUseCase.sendAuthEmail(request.username(), request.email());
    }

}
