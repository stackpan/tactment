package com.ivanzkyanto.tactment.resolver;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.annotation.Auth;
import com.ivanzkyanto.tactment.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @NonNull
    private UserRepository userRepository;

    private final ResponseStatusException UNAUTHORIZED_EXCEPTION = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class) && parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = request.getHeader("X-API-TOKEN");
        if (token == null) throw UNAUTHORIZED_EXCEPTION;

        return userRepository.findFirstByToken(token).orElseThrow(() -> UNAUTHORIZED_EXCEPTION);
    }
}
