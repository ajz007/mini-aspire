package com.miniaspire.loan.repository;

import com.miniaspire.loan.entity.Loan;
import com.miniaspire.loan.entity.LoanStatus;
import com.miniaspire.loan.repository.entity.LoanEntity;

import java.util.ArrayList;
import java.util.List;

public class LoanRepository {

    private ILoanRepository loanRepository;

    public LoanRepository(ILoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<Loan> getAllLoans() {
        var listOfLoans = new ArrayList<Loan>();
        for (LoanEntity loan : loanRepository.findAll()) {
            var loanRes = new Loan();
            loanRes.setLoanId(loan.loanId);
            loanRes.setLoanAmount(loan.loanAmount);
            loanRes.setAccount(loan.account);
            loanRes.setStatus(LoanStatus.fromValue(loan.status));
            loanRes.setInterestRate(loan.loanId);
            loanRes.setCreatedDate(loan.createdDate.toLocalDateTime().toLocalDate());
            listOfLoans.add(loanRes);
        }
        return listOfLoans;
    }

}
