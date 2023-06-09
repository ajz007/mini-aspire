package com.miniaspire.loan.exceptions;

import lombok.Data;

@Data
public class UnAuthorisedAccessException extends RuntimeException{

    private static final String CODE = "ERR_UNAUTH_ACCESS_1001";

    public UnAuthorisedAccessException(String message) {
        super(message);
    }
}
