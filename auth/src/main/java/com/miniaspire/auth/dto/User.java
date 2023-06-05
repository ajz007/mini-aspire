package com.miniaspire.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String username;
    /*private String loginId;
    @JsonIgnore
    private String password;
    private String email;*/
    private UserRole userRole;
}
