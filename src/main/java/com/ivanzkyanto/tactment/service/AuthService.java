package com.ivanzkyanto.tactment.service;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.ResetPasswordRequest;
import com.ivanzkyanto.tactment.model.request.UserLoginRequest;
import com.ivanzkyanto.tactment.model.response.UserLoginResponse;

public interface AuthService {

    UserLoginResponse login(UserLoginRequest request);

    void resetPassword(User user, ResetPasswordRequest request);

}
