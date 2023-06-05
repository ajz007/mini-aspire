package com.miniaspire.loan.repository.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "Loan")
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @Column(name = "login_id")
    public String loginId;

    @Column(unique = true)
    public String account;

    @Column(name = "loan_amount")
    public BigDecimal loanAmount;

    @Column(name = "term")
    public int term;

    @Column(name = "term_type")
    public int termType;

    @Column(name = "int_rate")
    public float interestRate;

    @Column
    public int status;

    @Column(name = "created_date", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
    public Timestamp createdDate;

    @OneToMany(cascade = CascadeType.ALL)
    public Set<RepaymentEntity> repayments;
}
