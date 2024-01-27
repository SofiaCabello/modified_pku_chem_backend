package com.example.pku_chem_backend.config;

import com.example.pku_chem_backend.util.JwtUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if(token == null) {
            // 暂时放行，生产环境应该把这段删掉。现在只是为了访问swagger-ui方便
            filterChain.doFilter(request, response);
            return;
        }
        if(JwtUtil.checkToken(token)) {
            // 拥有有效token的用户可以访问api
            filterChain.doFilter(request, response);
        } else {
            // 没有有效token的用户无法访问api
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("token无效");
        }
    }
}
