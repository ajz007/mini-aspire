package com.miniaspire.auth.service;

import com.miniaspire.auth.dto.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final RestTemplate template;

    private final JwtService jwtService;

    @Autowired
    public AuthService(JwtService jwtService, RestTemplate template) {
        this.jwtService = jwtService;
        this.template = template;
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public boolean validateUser(AuthRequest authRequest) {

        var requestEntity = new HttpEntity<>(authRequest);

        ResponseEntity<String> res = template.postForEntity("http://USER/user/validate",
                requestEntity, String.class);

        return res.getStatusCode().is2xxSuccessful();
    }
}
