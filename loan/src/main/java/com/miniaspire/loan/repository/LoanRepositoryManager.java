package com.miniaspire.loan.repository;

import com.miniaspire.loan.dto.Loan;
import com.miniaspire.loan.dto.LoanStatus;
import com.miniaspire.loan.dto.Repayment;
import com.miniaspire.loan.dto.RepaymentStatus;
import com.miniaspire.loan.repository.entity.LoanEntity;
import com.miniaspire.loan.repository.entity.RepaymentEntity;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LoanRepositoryManager {

    private final ILoanRepository loanRepository;

    public LoanRepositoryManager(ILoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<Loan> getAllLoans() {
        var listOfLoans = new ArrayList<Loan>();
        for (LoanEntity loan : loanRepository.findAll()) {
            var loanRes = new Loan();
            loanRes.setLoanId(loan.id);
            loanRes.setLoanAmount(loan.loanAmount);
            loanRes.setAccount(loan.account);
            loanRes.setStatus(LoanStatus.fromValue(loan.status));
            loanRes.setCreatedDate(loan.createdDate.toLocalDateTime());
            loanRes.setLoginId(loan.loginId);
            listOfLoans.add(loanRes);
        }
        return listOfLoans;
    }

    public List<Loan> getAllLoans(String username) {
        var listOfLoans = new ArrayList<Loan>();
        for (LoanEntity loan : loanRepository.findByLoginId(username)) {
            var loanRes = new Loan();
            loanRes.setLoanId(loan.id);
            loanRes.setLoanAmount(loan.loanAmount);
            loanRes.setAccount(loan.account);
            loanRes.setStatus(LoanStatus.fromValue(loan.status));
            loanRes.setCreatedDate(loan.createdDate.toLocalDateTime());
            loanRes.setLoginId(loan.loginId);
            listOfLoans.add(loanRes);
        }
        return listOfLoans;
    }

    public Loan saveLoan(Loan loanRequest) {

        if (loanRepository.findByAccount(loanRequest.getAccount()).isPresent()) {
            throw new RuntimeException("Loan account is already present");
        }
        try {
            var loanEntity = loanRepository.save(getLoanEntity(loanRequest));
            return getLoan(loanEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error creating loan account");
        }
    }

    public Loan getLoan(String loanAccount) {
        var res = loanRepository.findByAccount(loanAccount)
                .orElseThrow(() -> new RuntimeException("Loan account does not exist"));
        return getLoan(res);
    }

    public Loan getLoan(String loanAccount, String username) {
        var res = loanRepository.findByUsernameAndAccount(username, loanAccount)
                .orElseThrow(() -> new RuntimeException("Loan account does not exist"));
        return getLoan(res);
    }


    private LoanEntity getLoanEntity(Loan loan) {
        var loanEntity = new LoanEntity();
        loanEntity.loginId = loan.getLoginId();
        loanEntity.account = loan.getAccount();
        loanEntity.loanAmount = loan.getLoanAmount();
        loanEntity.term = loan.getTerm();
        loanEntity.status = loan.getStatus().getValue();
        loanEntity.createdDate = Timestamp.valueOf(Optional.ofNullable(loan.getCreatedDate())
                .orElse(LocalDateTime.now()));
        loanEntity.repayments = Optional.ofNullable(loan.getRepayments()).get()
                .stream().map(repayment -> {
                    var repaymentEntity = new RepaymentEntity();
                    repaymentEntity.amount = repayment.getAmount();
                    repaymentEntity.due_date = Date.valueOf(repayment.getDueDate());
                    repaymentEntity.status = repayment.getStatus().getValue();
                    return repaymentEntity;
                }).collect(Collectors.toSet());
        return loanEntity;
    }

    private Loan getLoan(LoanEntity loanEntity) {
        var loan = new Loan();
        loan.setLoginId(loanEntity.loginId);
        loan.setAccount(loanEntity.account);
        loan.setLoanAmount(loanEntity.loanAmount);
        loan.setTerm(loanEntity.term);
        loan.setStatus(LoanStatus.fromValue(loanEntity.status));
        loan.repayments = Optional.ofNullable(loanEntity.repayments).get()
                .stream().map(repaymentEntity -> {
                    var repayment = new Repayment();
                    //repaymentEntity.loanId = loan.getLoginId();
                    repayment.setAmount(repaymentEntity.amount);
                    repayment.setDueDate(repaymentEntity.due_date.toLocalDate());
                    repayment.setStatus(RepaymentStatus.fromValue(repaymentEntity.status));
                    return repayment;
                }).collect(Collectors.toSet());
        return loan;
    }

}
