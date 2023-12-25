package com.ivanzkyanto.tactment.service;

import com.ivanzkyanto.tactment.model.request.UserLoginRequest;
import com.ivanzkyanto.tactment.model.response.UserLoginResponse;

public interface AuthService {

    UserLoginResponse login(UserLoginRequest request);

}
