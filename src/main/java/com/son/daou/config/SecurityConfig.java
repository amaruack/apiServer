package com.son.daou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] IGNORE_URL_ARRAY = {
            "/shop-history",
            "/shop-history/**",
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        List<String> list = new ArrayList<>(List.of(IGNORE_URL_ARRAY));
        return (web) -> web.ignoring().antMatchers(list.toArray(new String[list.size()]));
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
                    .configurationSource(corsConfigurationSource())
                    .and()
                .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
        ;
        return http.build();
    }

    // api-gateway 에서 cors 처리를 함으로 주석
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(
//                "http://210.97.42.250:[*]",
//                "http://192.168.0.*:[*]",
                "http://*.*.*.*:[*]"
        ));
//        configuration.setAllowedOrigins(Arrays.asList("*")); // allowCredentials true 로 인하여 originpattern
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}