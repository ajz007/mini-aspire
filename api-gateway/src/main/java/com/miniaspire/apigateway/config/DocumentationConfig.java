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

    /*@Bean
    public List<GroupedOpenApi> apis() {
        List<GroupedOpenApi> groups = new ArrayList<>();
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*_route")).forEach(routeDefinition -> {
            String name = routeDefinition.getId().replaceAll("_route", "");
            System.out.println("name = "+name);
            GroupedOpenApi api = GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
            groups.add(api);
        });
        return groups;
    }*/

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r -> r.path("/user/v3/api-docs").and().method(HttpMethod.GET).uri("lb://USER"))
                .route(r -> r.path("/loan/v3/api-docs").and().method(HttpMethod.GET).uri("lb://LOAN"))
    .build();
    }

}
