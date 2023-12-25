package com.ivanzkyanto.tactment.service.impl;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.util.Token;
import com.ivanzkyanto.tactment.model.request.UserLoginRequest;
import com.ivanzkyanto.tactment.model.response.UserLoginResponse;
import com.ivanzkyanto.tactment.repository.UserRepository;
import com.ivanzkyanto.tactment.security.BCrypt;
import com.ivanzkyanto.tactment.service.AuthService;
import com.ivanzkyanto.tactment.service.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;

    private ValidationService validationService;

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        validationService.validate(request);

        ResponseStatusException invalidCredentialsException = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is wrong");

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> invalidCredentialsException);

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw invalidCredentialsException;
        }

        Token token = Token.generate();

        user.setToken(token.getToken());
        user.setTokenExpiredAt(token.getExpiredAt());
        userRepository.save(user);

        return UserLoginResponse.build(token);
    }

}
