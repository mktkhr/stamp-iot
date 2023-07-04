package com.example.stamp_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public AppInterceptor appInterceptor() throws Exception {
        return new AppInterceptor();
    }
}
