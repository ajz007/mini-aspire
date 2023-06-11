package com.miniaspire.loan.dto;

import com.miniaspire.loan.exceptions.InvalidInputException;

public enum RepaymentFrequency {
    WEEKLY(0);

    private int value;

    RepaymentFrequency(int value) {
        this.value = value;
    }

    public static RepaymentFrequency fromValue(int value) {
        switch (value) {
            case 0:
                return WEEKLY;
            default:
                throw new InvalidInputException("Unknown value for RepaymentFrequency");
        }
    }

    public int getValue() {
        return value;
    }
}
