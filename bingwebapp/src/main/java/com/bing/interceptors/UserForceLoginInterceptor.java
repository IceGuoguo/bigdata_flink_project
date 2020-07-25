package com.bing.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserForceLoginInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER=LoggerFactory.getLogger(UserForceLoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        LOGGER.debug(request.getRequestURL().toString());

        Object user = request.getSession().getAttribute("user");
        if(user==null){
            LOGGER.debug("转到登陆页:"+request.getRequestURL().toString());
            response.sendRedirect("/login.jsp");
        }
        return true;
    }
}
