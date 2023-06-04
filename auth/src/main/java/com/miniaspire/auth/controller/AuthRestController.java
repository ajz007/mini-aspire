package com.miniaspire.auth.controller;

import com.miniaspire.auth.dto.AuthRequest;
import com.miniaspire.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private AuthService authService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthRestController(AuthService authService,
                              AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @PostMapping("/login")
    public String getToken(@RequestBody AuthRequest authRequest) {
        if(authService.validateUser(authRequest)) {
            return authService.generateToken(authRequest.getUsername());
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return ResponseEntity
                .ok("Token is Valid");
    }

}
