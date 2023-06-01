package com.miniaspire.loan.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Repayment {

    private int loanId;

    private BigDecimal amount;

    private LocalDateTime due_date;

    private RepaymentStatus status;

    private LocalDateTime createdDate;

    public int getLoanId() {
        return loanId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repayment repayment = (Repayment) o;
        return loanId == repayment.loanId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId);
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDateTime due_date) {
        this.due_date = due_date;
    }

    public RepaymentStatus getStatus() {
        return status;
    }

    public void setStatus(RepaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
