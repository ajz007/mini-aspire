package com.miniaspire.user.controller;

import com.miniaspire.user.dto.AuthRequest;
import com.miniaspire.user.dto.RegisterUser;
import com.miniaspire.user.dto.SuccessResponse;
import com.miniaspire.user.dto.User;
import com.miniaspire.user.exception.InvalidInputException;
import com.miniaspire.user.service.MiniAspireUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private final MiniAspireUserDetailsService userService;

    @Autowired
    public UserRestController(MiniAspireUserDetailsService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class)),
                            examples = {
                                    @ExampleObject("""
                                            [{"username": "ajishu","loginId": "ajishu","email": "email.com","userRole": "USER"}]
                                            """)
                            }
                    )}),
            @ApiResponse(responseCode = "403", description = "Please login to continue",
                    content = @Content)})
    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @Operation(summary = "Get a user's detail by his login id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Please login to continue",
                    content = @Content)})
    @GetMapping("/{loginId}")
    public ResponseEntity<User> getUser(@PathVariable String loginId) {
        return ResponseEntity.ok(userService.getUser(loginId));
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Created!",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = {
                                    @ExampleObject("""
                                            {"username": "ajishu",
                                            "loginId": "ajishu",
                                            "password": "pass",
                                            "email": "any@email.com",
                                            "userRole": "USER"
                                            }"""),
                                    @ExampleObject("""
                                              {"username": "admin",
                                              "loginId": "admin",
                                              "password": "pass",
                                              "email": "admin@email.com",
                                              "userRole": "ADMIN"
                                            }"""),
                            })}),
            @ApiResponse(responseCode = "400", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Please login to continue",
                    content = @Content)})
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> register(@RequestBody RegisterUser registerUser) {
        userService.registerUser(registerUser);
        return new ResponseEntity<>(new SuccessResponse("User Created"), HttpStatus.CREATED);
    }

    @Operation(summary = "Validate a user using loginId and pass. This does not generate a token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is valid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid User",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Please login to continue",
                    content = @Content)})
    @PostMapping("/validate")
    public ResponseEntity<SuccessResponse> validateCredentials(@RequestBody AuthRequest authRequest) {
        if (userService.validate(authRequest.getUsername(), authRequest.getPassword())) {
            return ResponseEntity.ok(new SuccessResponse("User is valid"));
        } else {
            throw new InvalidInputException("Invalid User");
        }

    }
}
