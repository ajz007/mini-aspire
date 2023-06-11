package com.miniaspire.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    private BigDecimal amount;

    private String loanAccount;

    @JsonIgnore
    private Integer repaymentId;

    @JsonIgnore
    private String paidBy;

}
