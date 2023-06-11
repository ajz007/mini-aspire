package com.miniaspire.loan.service;

import com.miniaspire.loan.dto.Loan;
import com.miniaspire.loan.dto.Repayment;
import com.miniaspire.loan.dto.RepaymentFrequency;
import com.miniaspire.loan.dto.RepaymentStatus;
import com.miniaspire.loan.exceptions.InvalidInputException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class RepaymentsManager {

    public Set<Repayment> createRepayments(Loan loan, RepaymentFrequency repaymentFrequency) {

        if (loan == null) throw new IllegalArgumentException();

        BigDecimal repayAmount = loan.getLoanAmount()
                .divide(BigDecimal.valueOf(loan.getTerm()), 6, RoundingMode.HALF_UP);
        LocalDate nextDate = LocalDate.now();
        var list = new HashSet<Repayment>();

        for (int i = 0; i < loan.getTerm(); i++) {
            var repayment = new Repayment();
            repayment.setCreatedDate(LocalDateTime.now());
            repayment.setAmount(repayAmount);
            nextDate = getNextDate(nextDate, repaymentFrequency);
            repayment.setDueDate(nextDate);
            repayment.setStatus(RepaymentStatus.PENDING);
            list.add(repayment);
        }
        return list;
    }

    private LocalDate getNextDate(LocalDate localDate,
                                  RepaymentFrequency repaymentFrequency) {
        if (repaymentFrequency.equals(RepaymentFrequency.WEEKLY)) {
            return localDate.plusDays(7);
        }
        throw new InvalidInputException("Unknown value for RepaymentFrequency");
    }
}
