package com.miniaspire.loan.dto;

import org.springframework.stereotype.Component;

public enum RepaymentFrequency {
    WEEKLY(0);

    private int value;

    RepaymentFrequency(int value) {
        this.value = value;
    }

    public static RepaymentFrequency fromValue(int value) {
        switch(value) {
            case 0: return WEEKLY;
        }
        throw new RuntimeException("Unknown value for RepaymentFrequency");
    }

    public int getValue() {
        return value;
    }
}
