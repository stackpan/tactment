package com.ivanzkyanto.tactment.controller;

import com.ivanzkyanto.tactment.model.request.UserLoginRequest;
import com.ivanzkyanto.tactment.model.response.ApiResponse;
import com.ivanzkyanto.tactment.model.response.UserLoginResponse;
import com.ivanzkyanto.tactment.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

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

}
