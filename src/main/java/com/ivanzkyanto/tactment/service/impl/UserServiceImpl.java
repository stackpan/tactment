package com.ivanzkyanto.tactment.service.impl;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.UserRegisterRequest;
import com.ivanzkyanto.tactment.model.response.UserResponse;
import com.ivanzkyanto.tactment.repository.UserRepository;
import com.ivanzkyanto.tactment.security.BCrypt;
import com.ivanzkyanto.tactment.service.UserService;
import com.ivanzkyanto.tactment.service.ValidationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private ValidationService validationService;

    @Transactional
    @Override
    public void register(UserRegisterRequest request) {
        validationService.validate(request);

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);
    }

    @Override
    public UserResponse get(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }
}
