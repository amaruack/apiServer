package com.son.daou.config;

import com.son.daou.config.interceptor.IpAddressAccessInterceptor;
import com.son.daou.config.interceptor.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    IpAddressAccessInterceptor ipAddressAccessInterceptor;

    @Autowired
    RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipAddressAccessInterceptor ).addPathPatterns("/**").order(5);
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/**").order(0);
    }
}