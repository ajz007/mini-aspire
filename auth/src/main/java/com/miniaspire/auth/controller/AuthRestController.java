package com.miniaspire.auth.controller;

import com.miniaspire.auth.dto.AuthRequest;
import com.miniaspire.auth.dto.AuthResponse;
import com.miniaspire.auth.exception.InvalidInputException;
import com.miniaspire.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private AuthService authService;

    @Autowired
    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login to the application. Generates a jwt token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Success!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid credentials",
                    content = @Content)})
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> getToken(@RequestBody AuthRequest authRequest) {
        if (authService.validateUser(authRequest)) {
            var user = authService.getUser(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse("Login Success!", authService.generateToken(authRequest.getUsername(), user.getUserRole())));
        } else {
            throw new InvalidInputException("Invalid credentials");
        }
    }

    @Operation(summary = "Validate a jwt token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is Valid",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                    content = @Content)})
    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return ResponseEntity.ok("Token is Valid");
    }

}
