package com.ivanzkyanto.tactment.service;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.UserRegisterRequest;
import com.ivanzkyanto.tactment.model.request.UserUpdateRequest;
import com.ivanzkyanto.tactment.model.response.UserResponse;

public interface UserService {

    void register(UserRegisterRequest request);

    UserResponse get(User user);

    UserResponse update(User user, UserUpdateRequest request);

}
