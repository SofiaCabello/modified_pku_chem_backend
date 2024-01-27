package com.example.pku_chem_backend.config;

import com.example.pku_chem_backend.util.JwtUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class TokenInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if(token == null){
            throw new RuntimeException("无token，请重新登录");
        }
        if(!JwtUtil.checkToken(token)){
            throw new RuntimeException("token验证失败，请重新登录");
        }
        return true;
    }
}
