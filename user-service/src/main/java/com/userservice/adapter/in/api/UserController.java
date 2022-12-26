package com.userservice.adapter.in.api;

import com.userservice.adapter.in.api.dto.SignUpRequest;
import com.userservice.adapter.in.api.dto.UserDtoMapper;
import com.userservice.adapter.in.api.dto.UserResponse;
import com.userservice.application.port.in.EmailUseCase;
import com.userservice.application.port.in.SignUpUseCase;
import com.userservice.application.port.in.dto.EmailAuthRequestDto;
import com.userservice.application.port.in.dto.SignUpRequestDto;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final SignUpUseCase signUpUseCase;
    private final EmailUseCase emailUseCase;
    private final UserDtoMapper userDtoMapper;


    public UserController(SignUpUseCase signUpUseCase, EmailUseCase emailUseCase) {
        this.signUpUseCase = signUpUseCase;
        this.emailUseCase = emailUseCase;
        userDtoMapper = new UserDtoMapper();
    }

    @PostMapping("/user")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest request) {
        SignUpRequestDto requestDto = userDtoMapper.toServiceDto(request);
        UserResponse userResponse = signUpUseCase.signUp(requestDto);
        emailUseCase.sendAuthEmail(userResponse.username(), userResponse.email());
        return ResponseEntity.status(HttpStatus.CREATED).body("OK!");
    }

    @GetMapping("/email")
    public void checkMail(String username, String email, String authCode) {
        signUpUseCase.checkAuthCode(new EmailAuthRequestDto(username, email, authCode));
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK!");
    }
}
