package com.bing;

import com.bing.interceptors.UserForceLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class UserWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new UserForceLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/formUserManager/registerUser",
                        "/formUserManager/userLogin",
                        "/kaptcha/**",
                        "/statics/**",
                        "/commons/**"
                );
    }
}
