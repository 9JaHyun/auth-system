package com.userservice.adapter.in.api;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.adapter.in.api.dto.SignUpRequest;
import com.userservice.adapter.in.api.dto.UserDtoMapper;
import com.userservice.adapter.in.api.dto.UserResponse;
import com.userservice.application.port.in.EmailUseCase;
import com.userservice.application.port.in.SignUpUseCase;
import com.userservice.application.port.in.dto.SignUpRequestDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private SignUpUseCase signUpUseCase;
    @MockBean private EmailUseCase emailUseCase;
    @MockBean private UserDtoMapper userDtoMapper;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void testSignUp() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest("username", "password", "nickname",
            "email@gmail.com");

        givenSignUpWillSuccess(signUpRequest);

        // when
        mockMvc.perform(post("/user")
                .header("Content-Type", "application/json")
                .content(objectMapper.writeValueAsBytes(signUpRequest)))
            .andExpect(status().isCreated());

        // then
        then(signUpUseCase).should()
            .signUp(eq(new SignUpRequestDto("username", "password", "nickname",
                "email@gmail.com")));

        then(emailUseCase).should().sendAuthEmail(anyString(), anyString());
    }

    @Test
    void testCheckMail() {
        // given

        // when

        // then
    }

    private void givenSignUpWillSuccess(SignUpRequest request) {
        UserResponse userResponse = Mockito.mock(UserResponse.class);
        given(userResponse.username()).willReturn(request.username());
        given(userResponse.email()).willReturn(request.email());
        given(signUpUseCase.signUp(userDtoMapper.toServiceDto(request)))
            .willReturn(userResponse);
    }
}