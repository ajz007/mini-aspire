package com.miniaspire.loan.repository.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "repayments")
public class RepaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @Column
    public BigDecimal amount;

    @Column(name = "due_date")
    public Date due_date;

    @Column
    public int status;

    @Column(name = "created_date")
    public Timestamp createdDate;

    @Column(name = "updated_date")
    public Timestamp updatedDate;

    @Column(name = "updated_by")
    public String updatedBy;
}
