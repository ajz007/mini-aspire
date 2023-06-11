package com.miniaspire.payment.repository.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name="Payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @Column(name = "loan_account")
    public String loanAccount;

    @Column(name = "repayment_id")
    public int repaymentId;

    @Column
    public BigDecimal amount;

    @Column
    public String currency;

    @Column(name = "paid_by")
    public String  paidBy;

    @Column(name = "payment_date")
    public Timestamp paymentDate;

    @Column(name = "created_date")
    public Timestamp createdDate;

}
