package com.suakang.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suakang.auth.service.AuthService;
import com.suakang.auth.util.filter.JwtAuthorizationFilter;
import com.suakang.auth.util.filter.JwtFilter;
import com.suakang.auth.util.filter.VerifyMemberFilter;
import com.suakang.auth.util.jwt.JwtProvider;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean verifyMemberFilter(AuthService authService, ObjectMapper mapper) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new VerifyMemberFilter(authService, mapper));
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/auth/login");
        filterRegistrationBean.addUrlPatterns("/members/login");

        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean jwtFilter(JwtProvider provider, AuthService authService, ObjectMapper mapper) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new
                FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtFilter(provider, authService, mapper));
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/auth/login");
        filterRegistrationBean.addUrlPatterns("/members/login");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean jwtAuthorizationFilter(JwtProvider provider, ObjectMapper mapper) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new
                FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtAuthorizationFilter(provider, mapper));
        filterRegistrationBean.addUrlPatterns("/auth/login");
        filterRegistrationBean.addUrlPatterns("/members/login");
        filterRegistrationBean.setOrder(3);
        return filterRegistrationBean;
    }
}