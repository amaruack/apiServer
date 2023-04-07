package com.son.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private Info customOpenApiInfo(){
        Info info = new Info();
        info.setTitle("API 명세서");
        info.description("API 명세서 입니다.");
        info.version("v1.0");
        return info;
    }

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(customOpenApiInfo());
    }

    @Bean
    public GroupedOpenApi daouApi() {
        String[] paths = {"/shop-history/**"};
        return GroupedOpenApi
                .builder()
                .group("api-server")
                .packagesToScan("com.son.daou.controller")
                .pathsToMatch(paths)
                .build();
    }

}