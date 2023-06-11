package com.miniaspire.user.controller;

import com.miniaspire.user.dto.AuthRequest;
import com.miniaspire.user.dto.RegisterUser;
import com.miniaspire.user.dto.User;
import com.miniaspire.user.service.MiniAspireUserDetailsService;
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

    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{loginId}")
    public ResponseEntity<User> getUser(@PathVariable String loginId) {
        //TODO: Throw exception if not found
        return ResponseEntity.ok(userService.getUser(loginId));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterUser registerUser) {
        userService.registerUser(registerUser);
        return new ResponseEntity<String>("User Created", HttpStatus.CREATED);
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateCredentials(@RequestBody AuthRequest authRequest) {
        if(userService.validate(authRequest
                .getUsername(), authRequest.getPassword())) {
            return ResponseEntity.ok("User is valid");
        } else {
            return ResponseEntity.badRequest().body("Invalid User");
        }

    }
}
