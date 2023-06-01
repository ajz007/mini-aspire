package com.miniaspire.loan.service;

import com.miniaspire.loan.entity.Loan;
import com.miniaspire.loan.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LoanService {

    Logger logger = LoggerFactory.getLogger(LoanService.class);

    private LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<Loan> getLoans() {
        //check if user has access
        return loanRepository.getAllLoans();
    }

}
