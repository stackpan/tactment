package com.ivanzkyanto.tactment.configuration;

import com.ivanzkyanto.tactment.resolver.UserArgumentResolver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    @NonNull
    private UserArgumentResolver userArgumentResolver;

    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(userArgumentResolver);
    }
}
