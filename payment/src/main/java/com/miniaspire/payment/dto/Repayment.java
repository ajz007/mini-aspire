package com.miniaspire.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Repayment {

    private int id;

    private BigDecimal amount;

    private LocalDate dueDate;

    private RepaymentStatus status = RepaymentStatus.PENDING;

    private LocalDateTime createdDate;

}
