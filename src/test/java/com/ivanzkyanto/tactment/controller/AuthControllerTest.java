package com.ivanzkyanto.tactment.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.ResetPasswordRequest;
import com.ivanzkyanto.tactment.model.request.UserLoginRequest;
import com.ivanzkyanto.tactment.model.response.ApiResponse;
import com.ivanzkyanto.tactment.model.response.ErrorResponse;
import com.ivanzkyanto.tactment.model.response.UserLoginResponse;
import com.ivanzkyanto.tactment.repository.UserRepository;
import com.ivanzkyanto.tactment.security.BCrypt;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void loginSuccess() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("John Doe");
        userRepository.save(user);

        UserLoginRequest request = UserLoginRequest.builder()
                .username("johndoe")
                .password("password")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk()
        ).andDo(result -> {
            ApiResponse<UserLoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertNotNull(response.getData().getToken());
            Assertions.assertNotNull(response.getData().getExpiredAt());
        });
    }

    @Test
    void loginUserNotFound() throws Exception {
        UserLoginRequest request = UserLoginRequest.builder()
                .username("johndoe")
                .password("password")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                MockMvcResultMatchers.status().isUnauthorized(),
                MockMvcResultMatchers.content().string(Matchers.containsString("Username or password is wrong"))
        );
    }

    @Test
    void loginUserWrongPassword() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("John Doe");
        userRepository.save(user);

        UserLoginRequest request = UserLoginRequest.builder()
                .username("johndoe")
                .password("wrongpassword")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                MockMvcResultMatchers.status().isUnauthorized(),
                MockMvcResultMatchers.content().string(Matchers.containsString("Username or password is wrong"))
        );
    }

    @Test
    void resetPasswordSuccess() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("John Doe");

        userRepository.save(user);

        UserLoginRequest loginRequest = UserLoginRequest.builder()
                .username("johndoe")
                .password("password")
                .build();

        MvcResult loginMvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andReturn();

        ApiResponse<UserLoginResponse> loginResponse = objectMapper.readValue(loginMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        ResetPasswordRequest resetPasswordRequest = ResetPasswordRequest.builder()
                .oldPassword("password")
                .newPassword("newpassword")
                .newPasswordConfirmation("newpassword")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", loginResponse.getData().getToken())
                        .content(objectMapper.writeValueAsString(resetPasswordRequest))
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content()
                        .json(objectMapper.writeValueAsString(
                                ApiResponse.builder().data("Password reset successfully").build()))
        );
    }

    @Test
    void resetPasswordWrongPassword() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("John Doe");

        userRepository.save(user);

        UserLoginRequest loginRequest = UserLoginRequest.builder()
                .username("johndoe")
                .password("password")
                .build();

        MvcResult loginMvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andReturn();

        ApiResponse<UserLoginResponse> loginResponse = objectMapper.readValue(loginMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        ResetPasswordRequest resetPasswordRequest = ResetPasswordRequest.builder()
                .oldPassword("wrongpassword")
                .newPassword("newpassword")
                .newPasswordConfirmation("newpassword")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", loginResponse.getData().getToken())
                        .content(objectMapper.writeValueAsString(resetPasswordRequest))
        ).andExpectAll(
                MockMvcResultMatchers.status().isUnauthorized(),
                MockMvcResultMatchers.content()
                        .json(objectMapper.writeValueAsString(
                                ErrorResponse.builder()
                                        .errors("Wrong password")
                                        .build()))
        );
    }

    @Test
    void resetPasswordNotConfirmed() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("John Doe");

        userRepository.save(user);

        UserLoginRequest loginRequest = UserLoginRequest.builder()
                .username("johndoe")
                .password("password")
                .build();

        MvcResult loginMvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andReturn();

        ApiResponse<UserLoginResponse> loginResponse = objectMapper.readValue(loginMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        ResetPasswordRequest resetPasswordRequest = ResetPasswordRequest.builder()
                .oldPassword("password")
                .newPassword("newpassword")
                .newPasswordConfirmation("wrongpassword")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", loginResponse.getData().getToken())
                        .content(objectMapper.writeValueAsString(resetPasswordRequest))
        ).andExpectAll(
                MockMvcResultMatchers.status().isUnauthorized(),
                MockMvcResultMatchers.content()
                        .json(objectMapper.writeValueAsString(
                                ErrorResponse.builder()
                                        .errors("Wrong password")
                                        .build()
                        ))
        );
    }

    @Test
    void logoutSuccess() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("John Doe");

        userRepository.save(user);

        UserLoginRequest loginRequest = UserLoginRequest.builder()
                .username("johndoe")
                .password("password")
                .build();

        MvcResult loginMvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andReturn();

        ApiResponse<UserLoginResponse> loginResponse = objectMapper.readValue(loginMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", loginResponse.getData().getToken())
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content()
                        .json(objectMapper.writeValueAsString(
                                ApiResponse.builder().data("Ok").build()))
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", loginResponse.getData().getToken())
        ).andExpectAll(
                MockMvcResultMatchers.status().isUnauthorized(),
                MockMvcResultMatchers.content().string(Matchers.containsString("Unauthorized"))
        );
    }
}
