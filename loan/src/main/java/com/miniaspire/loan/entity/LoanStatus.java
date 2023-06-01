package com.miniaspire.loan.entity;

public enum LoanStatus {
    PENDING(0), APPROVED(1), CLOSED(2);

    private int status;

    LoanStatus(int status) {
        this.status = status;
    }

    public static LoanStatus fromValue(int status) {
        switch(status) {
            case 0: return PENDING;
            case 1: return APPROVED;
            case 2: return CLOSED;
        }
        throw new RuntimeException("unknown status");
    }
}
