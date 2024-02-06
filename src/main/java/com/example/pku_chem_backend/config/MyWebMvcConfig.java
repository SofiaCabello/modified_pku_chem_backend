package com.example.pku_chem_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CORSInterceptor())
            .addPathPatterns("/**");
        registry.addInterceptor(new TokenInterceptor())
                .excludePathPatterns("/**") // 生产环境必须移除
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/v3/api-docs/**")
                .excludePathPatterns("/login/userLogin")
                .excludePathPatterns("/login/userInfo")
                .excludePathPatterns("/swagger-ui.html#/**");
    }
}
