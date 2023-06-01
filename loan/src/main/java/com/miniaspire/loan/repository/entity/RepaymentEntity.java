package com.miniaspire.loan.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class RepaymentEntity {
    @Id
    @Column
    public int id;

    @Column(name = "loan_id")
    public int loanId;

    @Column
    public BigDecimal amount;

    @Column(name = "due_date")
    public Timestamp due_date;

    @Column
    public String status;

    @Column(name = "created_date")
    public Timestamp createdDate;
}
