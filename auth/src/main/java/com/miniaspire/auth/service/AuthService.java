package com.miniaspire.auth.service;

import com.miniaspire.auth.dto.AuthRequest;
import com.miniaspire.auth.dto.SuccessResponse;
import com.miniaspire.auth.dto.User;
import com.miniaspire.auth.dto.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final RestTemplate template;

    private final JwtService jwtService;

    @Autowired
    public AuthService(JwtService jwtService, RestTemplate template) {
        this.jwtService = jwtService;
        this.template = template;
    }

    public String generateToken(String username, UserRole userRole) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("USER_ROLE", userRole.name());
        return jwtService.generateToken(username, claims);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public boolean validateUser(AuthRequest authRequest) {

        var requestEntity = new HttpEntity<>(authRequest);

        ResponseEntity<SuccessResponse> res = template.postForEntity("http://USER/user/validate",
                requestEntity, SuccessResponse.class);

        return res.getStatusCode().is2xxSuccessful();
    }

    public User getUser(String username) {

        ResponseEntity<User> res = template.getForEntity("http://USER/user/"+username,
                User.class);

        return res.getBody();
    }
}
