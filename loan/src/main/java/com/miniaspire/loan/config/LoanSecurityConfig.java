/*
package com.miniaspire.loan.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

public class LoanSecurityConfig {

    @Bean
    @LoadBalanced
    public RestTemplate template() {
        return new RestTemplate();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors().disable().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/loan/swagger-ui.html", "/loan/v3/api-docs/swagger-config", "/loan/v3/api-docs").permitAll()
                .requestMatchers("/h2-console", "/h2-console/*").permitAll()
                .anyRequest().permitAll()
                .and().formLogin().disable()
                .build();
    }

}
*/
