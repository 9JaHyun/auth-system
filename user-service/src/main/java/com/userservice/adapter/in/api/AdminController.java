package com.userservice.adapter.in.api;

import com.userservice.adapter.in.api.dto.UserResponse;
import com.userservice.application.port.in.GetUsersUseCase;
import com.userservice.domain.User;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private final GetUsersUseCase getUsersUseCase;

    public AdminController(GetUsersUseCase getUsersUseCase) {
        this.getUsersUseCase = getUsersUseCase;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = getUsersUseCase.getUsersInfo();

        List<UserResponse> userResponses = users.stream()
            .map(UserResponse::new)
            .toList();

        return ResponseEntity.ok(userResponses);
    }
}
