package com.ivanzkyanto.tactment.controller;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.annotation.Auth;
import com.ivanzkyanto.tactment.model.request.UserRegisterRequest;
import com.ivanzkyanto.tactment.model.request.UserUpdateRequest;
import com.ivanzkyanto.tactment.model.response.ApiResponse;
import com.ivanzkyanto.tactment.model.response.UserResponse;
import com.ivanzkyanto.tactment.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    @NonNull
    private UserService userService;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> register(@RequestBody UserRegisterRequest request) {
        userService.register(request);
        return ApiResponse.<String>builder().data("Ok").build();
    }

    @GetMapping(path = "/api/users/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<UserResponse> getCurrent(@Auth User user) {
        UserResponse response = userService.get(user);
        return ApiResponse.<UserResponse>builder().data(response).build();
    }

    @PutMapping(
            path = "/api/users/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<UserResponse> update(@Auth User user, @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.update(user, request);
        return ApiResponse.<UserResponse>builder().data(response).build();
    }

}
