package com.miniaspire.loan.exceptions;

import lombok.Data;

@Data
public class TechnicalUnExpectedException extends RuntimeException{

    private static final String CODE = "ERR_TECH_UNEXCEP__1001";

    public TechnicalUnExpectedException(String message) {
        super(message);
    }
}
