package com.miniaspire.loan.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class LoanEntity {
    @Id
    @Column
    public int id;

    @Column(name = "loan_id")
    public int loanId;

    @Column
    public String account;

    @Column(name = "loan_amount")
    public BigDecimal loanAmount;

    @Column(name = "int_rate")
    public float interestRate;

    @Column
    public int status;

    @Column(name = "created_date")
    public Timestamp createdDate;
}
