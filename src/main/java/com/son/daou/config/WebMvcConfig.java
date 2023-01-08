package com.son.daou.config;

import com.son.daou.config.interceptor.IpAddressAccessInterceptor;
import com.son.daou.config.interceptor.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String[] IGNORE_URL_ARRAY = {
            /* swagger v3 */
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/swagger-ui/**",
            "/swagger-ui",
            "/swagger-ui.html",
            "/h2-console/**",
            "/favicon.ico"
    };

    @Autowired
    IpAddressAccessInterceptor ipAddressAccessInterceptor;

    @Autowired
    RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipAddressAccessInterceptor ).addPathPatterns("/**")
                .excludePathPatterns(IGNORE_URL_ARRAY).order(5);
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/**")
                .excludePathPatterns(IGNORE_URL_ARRAY).order(0);
    }
}