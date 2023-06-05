package com.miniaspire.loan.service;

import com.miniaspire.loan.dto.Loan;
import com.miniaspire.loan.dto.Repayment;
import com.miniaspire.loan.dto.RepaymentFrequency;
import com.miniaspire.loan.dto.RepaymentStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class RepaymentsManager {

    public static Set<Repayment> createRepayments(Loan loan, RepaymentFrequency repaymentFrequency) {
        BigDecimal repayAmount = loan.getLoanAmount()
                .divide(BigDecimal.valueOf(loan.getTerm()), 6, RoundingMode.HALF_UP);
        LocalDate nextDate = LocalDate.now();
        var list = new HashSet<Repayment>();

        for (int i = 0; i < loan.getTerm(); i++) {
            var repayment = new Repayment();
            repayment.setCreatedDate(LocalDateTime.now());
            repayment.setAmount(repayAmount);
            nextDate = getNextDate(nextDate, RepaymentFrequency.WEEKLY);
            repayment.setDueDate(nextDate);
            repayment.setStatus(RepaymentStatus.PENDING);
            list.add(repayment);
        }
        return list;
    }

    private static LocalDate getNextDate(LocalDate localDate,
                                         RepaymentFrequency repaymentFrequency) {
        switch (repaymentFrequency) {
            case WEEKLY -> {
                return localDate.plusDays(7);
            }
            case MONTHLY -> {
                //check for specific month, month in leap year later
                return localDate.plusDays(30);
            }
        }
        return localDate;
    }
}
