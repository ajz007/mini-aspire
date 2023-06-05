package com.miniaspire.apigateway.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiGatewayConfig {
    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }
}
