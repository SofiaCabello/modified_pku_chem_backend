package com.example.pku_chem_backend.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class ApiPrefixConfig {

    @Bean
    public ServletRegistrationBean<DispatcherServlet> apiServletRegistration(ApplicationContext applicationContext) {
        DispatcherServlet apiServlet = new DispatcherServlet();
        apiServlet.setApplicationContext(applicationContext);
        ServletRegistrationBean<DispatcherServlet> registrationBean = new ServletRegistrationBean<>(apiServlet, "/api/*");
        registrationBean.setName("api");
        return registrationBean;
    }
}