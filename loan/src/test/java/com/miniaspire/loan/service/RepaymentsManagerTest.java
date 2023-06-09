package com.miniaspire.loan.service;

import com.miniaspire.loan.dto.Loan;
import com.miniaspire.loan.dto.RepaymentFrequency;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class RepaymentsManagerTest {


    private RepaymentsManager repaymentsManager = new RepaymentsManager();

    @Test
    public void createRepaymentsSunnyDay() {
        var repayments = repaymentsManager
                .createRepayments(getLoan(BigDecimal.valueOf(10000), 3),
                        RepaymentFrequency.WEEKLY);
        Assert.assertEquals(3, repayments.size());
    }

    @Test
    public void createRepaymentsWithNullLoanThrowsException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> repaymentsManager
                .createRepayments(null,
                        RepaymentFrequency.WEEKLY));
    }

    private Loan getLoan(BigDecimal loanAmount, int loanTerm) {
        var loan = new Loan();
        loan.setLoanAmount(loanAmount);
        loan.setTerm(loanTerm);
        return loan;
    }
}
