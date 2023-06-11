package com.miniaspire.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvalidInputException extends RuntimeException {

    private static final String CODE = "ERR_INV_INP_1001";

    public InvalidInputException(String message) {
        super(message);
    }
}
