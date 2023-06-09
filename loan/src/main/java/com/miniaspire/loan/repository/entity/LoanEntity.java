package com.miniaspire.loan.repository.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
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

    @Column
    public int status;

    @Column(name = "created_date", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
    public Timestamp createdDate;

    @OneToMany(cascade = CascadeType.ALL)
    public Set<RepaymentEntity> repayments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanEntity that = (LoanEntity) o;
        return account.equals(that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account);
    }
}
