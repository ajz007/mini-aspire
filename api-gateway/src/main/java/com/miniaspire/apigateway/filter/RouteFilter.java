package com.miniaspire.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteFilter {

    public static final List<String> openApiEndpoints = List.of("/auth/register", "/auth/token", "/auth/register", "/auth/token", "/auth/validate", "/auth/swagger", "/auth/swagger/*", "/auth/swagger-ui", "/auth/swagger-ui/*", "/v3/api-docs", "v3/api-docs/*", "/user/register", "/user/validate", "/user/swagger", "/user/swagger/*", "/user/swagger-ui", "/user/swagger-ui/*", "/v3/api-docs", "v3/api-docs/swagger-config", "v3/api-docs/*", "/eureka");

    public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));

}
