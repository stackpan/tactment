package com.ivanzkyanto.tactment.controller;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.annotation.Auth;
import com.ivanzkyanto.tactment.model.request.ResetPasswordRequest;
import com.ivanzkyanto.tactment.model.request.UserLoginRequest;
import com.ivanzkyanto.tactment.model.response.ApiResponse;
import com.ivanzkyanto.tactment.model.response.UserLoginResponse;
import com.ivanzkyanto.tactment.service.AuthService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @NonNull
    private AuthService authService;

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        UserLoginResponse response = authService.login(request);
        return ApiResponse.<UserLoginResponse>builder().data(response).build();
    }

    @PatchMapping(
            path = "/api/auth/reset-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private ApiResponse<String> resetPassword(@Auth User user, @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(user, request);
        return ApiResponse.<String>builder().data("Password reset successfully").build();
    }

    @PostMapping(
            path = "/api/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<String> logout(@Auth User user) {
        authService.logout(user);
        return ApiResponse.<String>builder().data("Ok").build();
    }
}
