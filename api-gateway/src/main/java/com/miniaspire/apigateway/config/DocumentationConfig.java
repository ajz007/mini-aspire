package com.miniaspire.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
@RequiredArgsConstructor
public class DocumentationConfig {

    private final RouteDefinitionLocator locator;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r -> r.path("/user/v3/api-docs").and().method(HttpMethod.GET).uri("lb://USER"))
                .route(r -> r.path("/loan/v3/api-docs").and().method(HttpMethod.GET).uri("lb://LOAN"))
    .build();
    }

}
