package com.miniaspire.apigateway.filter;


import com.miniaspire.apigateway.service.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private RouteFilter validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Please login to the application");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    var claims = jwtUtil.validateToken(authHeader);
                    updateHeaders(claims, exchange);
                } catch (Exception e) {
                    //throw new RuntimeException("Unauthorized access or token expired. Please login. " + e.getMessage());
                    throw new ResponseStatusException(HttpStatusCode.valueOf(403));
                }
            }
            return chain.filter(exchange);
        });
    }

    private void updateHeaders(Jws<Claims> claims,
                               ServerWebExchange exchange) {
        exchange.getRequest().mutate()
                .header("x-user_name", claims.getBody().get("sub").toString()).build();
        exchange.getRequest().mutate()
                .header("x-user_role", claims.getBody().get("USER_ROLE").toString()).build();
    }

    public static class Config {
    }
}
