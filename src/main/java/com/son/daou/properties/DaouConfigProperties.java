package com.son.daou.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "daou")
public class DaouConfigProperties {

    private String rootPath;
    private List<String> accessIpAddress;
    private RateLimit rateLimit;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RateLimit {
        private Integer capacity;
        private Integer time;
        private String unit;
    }

}