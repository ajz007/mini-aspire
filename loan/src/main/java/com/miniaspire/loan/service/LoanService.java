package com.miniaspire.loan.service;

import com.miniaspire.loan.dto.*;
import com.miniaspire.loan.repository.LoanRepositoryManager;
import com.miniaspire.loan.repository.RepaymentsRepositoryManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    private LoanRepositoryManager loanRepositoryManager;
    private RepaymentsRepositoryManager repaymentsRepositoryManager;

    public LoanService(LoanRepositoryManager loanRepositoryManager, RepaymentsRepositoryManager repaymentsRepositoryManager) {
        this.loanRepositoryManager = loanRepositoryManager;
        this.repaymentsRepositoryManager = repaymentsRepositoryManager;
    }

    public List<Loan> getLoans(String userRole) {
        if (userRole!=null && !userRole.equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("You do not have access to this service");
        }
        return loanRepositoryManager.getAllLoans();
    }

    public List<Loan> getUserLoans(String username, String userRole) {
        if (userRole!=null && userRole.equalsIgnoreCase("USER")) {
            return loanRepositoryManager.getAllLoans(username);
        } else if(userRole!=null && userRole.equalsIgnoreCase("USER")) {
            return loanRepositoryManager.getAllLoans();
        }
        throw new RuntimeException("You do not have sufficient access to this service");
    }

    public Loan getLoan(String username, String userRole, String loanAccount) {
        if (userRole != null && userRole.equalsIgnoreCase("ADMIN")) {
            return loanRepositoryManager.getLoan(loanAccount);
        } else {
            return loanRepositoryManager.getLoan(username, loanAccount);
        }
    }

    public LoanCreateRes createLoan(Loan loan) {
        if (!loan.getUserRole().equalsIgnoreCase("USER")) {
            throw new RuntimeException("You do not have sufficient access for this service");
        }
        var repayments = RepaymentsManager.createRepayments(loan, RepaymentFrequency.WEEKLY);
        loan.setRepayments(repayments);
        var loanres = loanRepositoryManager.saveLoan(loan);

        return new LoanCreateRes(loanres.getAccount(), loanres.getLoanAmount(), loanres.getTerm(), "Loan created successfully");
    }

    public List<Repayment> getRepayments(String username, String userRole, String loanAccount) {
        if(userRole!=null && userRole.equalsIgnoreCase("ADMIN")) {
            return repaymentsRepositoryManager.getRepayments(loanAccount);
        } else {
            return repaymentsRepositoryManager.getRepayments(username, loanAccount);
        }
    }

    public void updateLoanStatus(String userRole, String loanAccount,
                                 String loanStatus) {
        if (userRole !=null && !userRole.equalsIgnoreCase("ADMIN")) {
            throw  new RuntimeException("You do not have sufficient access for this service");
        }
        if(loanStatus == null) {
            throw new RuntimeException("Unknown value for LoanStatus");
        }

        var loan = loanRepositoryManager.getLoan(loanAccount);
        loan.setStatus(LoanStatus.valueOf(loanStatus));
        loanRepositoryManager.saveLoan(loan);
    }

}
