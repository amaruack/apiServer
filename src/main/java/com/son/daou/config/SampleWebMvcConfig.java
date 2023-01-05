package com.son.daou.config;

import com.son.daou.config.interceptor.IpAddressAccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SampleWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    IpAddressAccessInterceptor ipAddressAccessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipAddressAccessInterceptor).addPathPatterns("/**");
    }
}