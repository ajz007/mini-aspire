package com.miniaspire.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String username;
    private String loginId;
    @JsonIgnore()
    private String password;
    private String email;
    private UserRole userRole;

    public static User from(RegisterUser registerUser) {
        return new User(registerUser.getUsername(), registerUser.getLoginId(),
                registerUser.getPassword(), registerUser.getEmail(), registerUser.getUserRole());
    }
}
