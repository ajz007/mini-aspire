package com.miniaspire.user.dto;

import com.miniaspire.user.exception.InvalidInputException;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum UserRole {

    USER(0), ADMIN(1);
    private int role;

    UserRole(int role) {
        this.role = role;
    }

    public static UserRole fromValue(int role) {
        switch (role) {
            case 0:
                return USER;
            case 1:
                return ADMIN;
            default:
                throw new InvalidInputException("Invalid role for the user");
        }

    }

    public int getValue() {
        return this.role;
    }

}
