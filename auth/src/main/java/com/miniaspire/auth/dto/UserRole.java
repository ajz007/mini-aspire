package com.miniaspire.auth.dto;

public enum UserRole {

    USER(0), ADMIN(1);
    private int role;

    UserRole(int role) {
        this.role = role;
    }

    public static UserRole fromValue(int role) {
        switch(role) {
            case 0: return USER;
            case 1: return ADMIN;
        }
        throw new RuntimeException("Invalid role for the user");
    }

    public int getValue() {
        return this.role;
    }

}
