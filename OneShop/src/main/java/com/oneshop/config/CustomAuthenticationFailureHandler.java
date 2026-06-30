package com.oneshop.config;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        AuthenticationException exception) 
            throws IOException, ServletException {
        
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            if (exception instanceof DisabledException) {
                response.getWriter().write("{\"success\": false, \"message\": \"Tài khoản chưa được kích hoạt\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Tài khoản hoặc mật khẩu không chính xác!\"}");
            }
        } else {
            if (exception instanceof DisabledException) {
                response.sendRedirect("/login?error=disabled");
            } else {
                response.sendRedirect("/login?error=true");
            }
        }
    }
}