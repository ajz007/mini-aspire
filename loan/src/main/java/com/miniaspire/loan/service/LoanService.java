package com.miniaspire.loan.service;

import com.miniaspire.loan.dto.*;
import com.miniaspire.loan.exceptions.InvalidInputException;
import com.miniaspire.loan.exceptions.UnAuthorisedAccessException;
import com.miniaspire.loan.repository.LoanRepositoryManager;
import com.miniaspire.loan.repository.RepaymentsRepositoryManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    private static final String USER_ROLE_ADMIN = "ADMIN";
    private static final String USER_ROLE_CUSTOMER = "USER";
    private static final String SERVICE_ROLE = "true";

    private final LoanRepositoryManager loanRepositoryManager;
    private final RepaymentsRepositoryManager repaymentsRepositoryManager;
    private final RepaymentsManager repaymentsManager;

    public LoanService(LoanRepositoryManager loanRepositoryManager,
                       RepaymentsRepositoryManager repaymentsRepositoryManager,
                       RepaymentsManager repaymentsManager) {
        this.loanRepositoryManager = loanRepositoryManager;
        this.repaymentsRepositoryManager = repaymentsRepositoryManager;
        this.repaymentsManager = repaymentsManager;
    }

    public List<Loan> getUserLoans(String username, String userRole) {
        if (userRole != null && userRole.equalsIgnoreCase(USER_ROLE_CUSTOMER)) {
            return loanRepositoryManager.getAllLoans(username);
        } else if (userRole != null && userRole.equalsIgnoreCase(USER_ROLE_ADMIN)) {
            return loanRepositoryManager.getAllLoans();
        }
        throw new UnAuthorisedAccessException("You do not have sufficient access to this service");
    }

    public Loan getLoan(String username, String userRole, String loanAccount) {
        if (userRole != null && userRole.equalsIgnoreCase(USER_ROLE_ADMIN)) {
            return loanRepositoryManager.getLoan(loanAccount);
        } else {
            return loanRepositoryManager.getLoan(username, loanAccount);
        }
    }

    public LoanCreateRes createLoan(Loan loan) {
        if (!loan.getUserRole().equalsIgnoreCase(USER_ROLE_CUSTOMER)) {
            throw new UnAuthorisedAccessException("You do not have sufficient access for this service");
        }
        var repayments = repaymentsManager
                .createRepayments(loan, RepaymentFrequency.WEEKLY);
        loan.setRepayments(repayments);
        var loanres = loanRepositoryManager.createLoan(loan);

        return new LoanCreateRes(loanres.getAccount(), loanres.getLoanAmount(), loanres.getTerm(), "Loan created successfully");
    }

    public List<Repayment> getRepayments(String username, String userRole, String loanAccount, String repaymentStatus) {
        if (userRole != null && userRole.equalsIgnoreCase(USER_ROLE_ADMIN)) {
            return repaymentsRepositoryManager.getRepayments(loanAccount, repaymentStatus);
        } else {
            return repaymentsRepositoryManager.getRepayments(username, loanAccount, repaymentStatus);
        }
    }

    public void updateLoanStatus(String userRole, String serviceRole, String loanAccount,
                                 String loanStatus) {
        if (loanStatus == null) {
            throw new InvalidInputException("Unknown value for LoanStatus");
        }
        if (userRole != null && (!userRole.equalsIgnoreCase(USER_ROLE_ADMIN) ||
                !serviceRole.equalsIgnoreCase(SERVICE_ROLE))) {
            throw new UnAuthorisedAccessException("You do not have sufficient access for this service");
        }
        var loan = loanRepositoryManager.getLoan(loanAccount);
        loan.setStatus(LoanStatus.valueOf(loanStatus));
        loanRepositoryManager.updateLoan(loan);
    }

    public void updateRepaymentStatus(String username, String userRole, String serviceRole,
                                      Repayment repayment) {
        if (repayment.getStatus() == null) {
            throw new InvalidInputException("Unknown value for RepaymentStatus");
        }
        if ((userRole != null && (!userRole.equalsIgnoreCase(USER_ROLE_ADMIN))
                || (serviceRole!=null && !serviceRole.equalsIgnoreCase(SERVICE_ROLE) )) ) {
            throw new UnAuthorisedAccessException("You do not have sufficient access for this service");
        }
        repaymentsRepositoryManager.updateRepayment(repayment, username);
    }
}
