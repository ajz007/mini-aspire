package com.miniaspire.loan.repository;

import com.miniaspire.loan.dto.Repayment;
import com.miniaspire.loan.dto.RepaymentStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RepaymentsRepositoryManager {

    private IRepaymentsRepository repaymentsRepository;
    private ILoanRepository loanRepository;

    public RepaymentsRepositoryManager(IRepaymentsRepository repaymentsRepository) {
        this.repaymentsRepository = repaymentsRepository;
    }

    public List<Repayment> getRepayments(String loanAccount) {
        var loan = loanRepository.findByAccount(loanAccount)
                .orElseThrow(() -> new RuntimeException("Loan account not found"));
        var list = loan.repayments;
        return Optional.ofNullable(list).get()
                .stream().map(repaymentEntity -> {
                    var repayment = new Repayment();
                    repayment.setAmount(repaymentEntity.amount);
                    repayment.setDueDate(repaymentEntity.due_date.toLocalDate());
                    repayment.setStatus(RepaymentStatus.fromValue(repaymentEntity.status));
                    return repayment;
                }).collect(Collectors.toList());
    }

    public List<Repayment> getRepayments(String username, String loanAccount) {
        var loan = loanRepository.findByUsernameAndAccount(username, loanAccount)
                .orElseThrow(() -> new RuntimeException("Loan account not found"));
        var list = loan.repayments;
        return Optional.ofNullable(list).get()
                .stream().map(repaymentEntity -> {
                    var repayment = new Repayment();
                    repayment.setAmount(repaymentEntity.amount);
                    repayment.setDueDate(repaymentEntity.due_date.toLocalDate());
                    repayment.setStatus(RepaymentStatus.fromValue(repaymentEntity.status));
                    return repayment;
                }).collect(Collectors.toList());
    }

}


