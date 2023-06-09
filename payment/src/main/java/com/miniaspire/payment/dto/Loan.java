package com.miniaspire.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @JsonIgnore
    private int loanId;

    private String loginId;

    @JsonIgnore
    private String userRole;

    private String account;

    private BigDecimal loanAmount;

    private int term;

    private LoanStatus status = LoanStatus.PENDING;

    private LocalDateTime createdDate;

    @JsonIgnore
    public Set<Repayment> repayments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return loanId == loan.loanId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId);
    }
}
