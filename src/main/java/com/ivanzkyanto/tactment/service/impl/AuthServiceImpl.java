package com.ivanzkyanto.tactment.service.impl;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.ResetPasswordRequest;
import com.ivanzkyanto.tactment.util.Token;
import com.ivanzkyanto.tactment.model.request.UserLoginRequest;
import com.ivanzkyanto.tactment.model.response.UserLoginResponse;
import com.ivanzkyanto.tactment.repository.UserRepository;
import com.ivanzkyanto.tactment.security.BCrypt;
import com.ivanzkyanto.tactment.service.AuthService;
import com.ivanzkyanto.tactment.service.ValidationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @NonNull
    private UserRepository userRepository;

    @NonNull
    private ValidationService validationService;

    private final ResponseStatusException INVALID_CREDENTIALS_EXCEPTION = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is wrong");

    @Override
    @Transactional
    public UserLoginResponse login(UserLoginRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> INVALID_CREDENTIALS_EXCEPTION);

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw INVALID_CREDENTIALS_EXCEPTION;
        }

        Token token = Token.generate();

        user.setToken(token.getToken());
        user.setTokenExpiredAt(token.getExpiredAt());
        userRepository.save(user);

        return UserLoginResponse.build(token);
    }

    @Override
    @Transactional
    public void resetPassword(User user, ResetPasswordRequest request) {
        validationService.validate(request);

        boolean passwordMatch = BCrypt.checkpw(request.getOldPassword(), user.getPassword());
        boolean passwordConfirmed = request.getNewPasswordConfirmation().equals(request.getNewPassword());

        if (!passwordMatch || !passwordConfirmed) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password");
        }

        user.setPassword(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));
    }

    @Override
    @Transactional
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepository.save(user);
    }

}
