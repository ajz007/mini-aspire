package com.miniaspire.auth.dto;

import com.miniaspire.auth.exception.InvalidInputException;

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
