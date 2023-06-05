package com.miniaspire.auth.controller;

import com.miniaspire.auth.dto.AuthRequest;
import com.miniaspire.auth.dto.AuthResponse;
import com.miniaspire.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private AuthService authService;
    //private AuthenticationManager authenticationManager;

    @Autowired
    public AuthRestController(AuthService authService) {
        //this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> getToken(@RequestBody AuthRequest authRequest) {
        if (authService.validateUser(authRequest)) {
            var user = authService.getUser(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse("Login Success!",
                    authService.generateToken(authRequest.getUsername(), user.getUserRole())));
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return ResponseEntity
                .ok("Token is Valid");
    }

}
