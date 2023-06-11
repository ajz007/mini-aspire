package com.miniaspire.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCreateRes {

    private String account;

    private BigDecimal loanAmount;

    private int term;

    private String msg;

}
