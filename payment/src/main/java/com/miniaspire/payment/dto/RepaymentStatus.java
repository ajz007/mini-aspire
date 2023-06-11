package com.miniaspire.payment.dto;

public enum RepaymentStatus {
    PENDING(0), PAID(1);

    private int status;

    RepaymentStatus(int status) {
        this.status = status;
    }

    public static RepaymentStatus fromValue(int status) {
        switch(status) {
            case 0: return PENDING;
            case 1: return PAID;
        }
        throw new RuntimeException("Unknown value for RepaymentStatus");
    }

    public int getValue() {
        return status;
    }

}
