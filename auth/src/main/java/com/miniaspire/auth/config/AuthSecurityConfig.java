package com.miniaspire.auth.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class AuthSecurityConfig {

    @Bean
    @LoadBalanced
    public RestTemplate template() {
        return new RestTemplate();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/register", "/auth/token", "/auth/validate").permitAll()
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/auth/swagger-ui.html", "auth/v3/api-docs/swagger-config", "/auth/v3/api-docs").permitAll()
                .requestMatchers("/h2-console", "/h2-console/*").permitAll()
                .and()
                .build();
    }

}
