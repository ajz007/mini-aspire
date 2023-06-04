package com.miniaspire.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String username;
    private String loginId;
    private String password;
    private String email;
    private UserRole userRole;
}
