package com.miniaspire.loan.repository;

import com.miniaspire.loan.dto.Repayment;
import com.miniaspire.loan.dto.RepaymentStatus;
import com.miniaspire.loan.exceptions.InvalidInputException;
import com.miniaspire.loan.exceptions.TechnicalUnExpectedException;
import com.miniaspire.loan.repository.entity.RepaymentEntity;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RepaymentsRepositoryManager {

    private IRepaymentsRepository repaymentsRepository;
    private ILoanRepository loanRepository;

    public RepaymentsRepositoryManager(IRepaymentsRepository repaymentsRepository, ILoanRepository loanRepository) {
        this.repaymentsRepository = repaymentsRepository;
        this.loanRepository = loanRepository;
    }

    public void updateRepayment(Repayment repayment, String username) {
        var repayDbObj = repaymentsRepository.findById(repayment.getId());
        if (!repayDbObj.isPresent()) {
            throw new InvalidInputException("Invalid repayment id");
        }
        repayDbObj.ifPresent(repaymentEntity -> {
            repaymentEntity.amount = repayment.getAmount();
            repaymentEntity.status = repayment.getStatus().getValue();
            repaymentEntity.updatedDate = Timestamp.valueOf(LocalDateTime.now());
            repaymentEntity.updatedBy = username;
            try {
                repaymentsRepository.save(repaymentEntity);
            } catch (Exception e) {
                throw new TechnicalUnExpectedException("Update repayment failed");
            }
        });
    }

    public List<Repayment> getRepayments(String loanAccount, String repaymentStatus) {
        var loan = loanRepository.findByAccount(loanAccount).orElseThrow(() -> new RuntimeException("Loan account not found"));
        var list = loan.repayments;

        return Optional.ofNullable(list).map(repaymentEntities -> repaymentEntities.stream().map(repaymentEntity -> {
            var repayment = new Repayment();
            repayment.setId(repaymentEntity.id);
            repayment.setAmount(repaymentEntity.amount);
            repayment.setDueDate(repaymentEntity.due_date.toLocalDate());
            repayment.setStatus(RepaymentStatus.fromValue(repaymentEntity.status));
            repayment.setCreatedDate(repaymentEntity.createdDate.toLocalDateTime());
            return repayment;
        }).filter(repayment -> {
            if (repaymentStatus != null && RepaymentStatus.valueOf(repaymentStatus) != null) {
                return repayment.getStatus().equals(RepaymentStatus.valueOf(repaymentStatus));
            } else {
                return true;
            }
        }).toList()).orElse(new ArrayList<>());
    }

    public List<Repayment> getRepayments(String username, String loanAccount, String repaymentStatus) {
        var loan = loanRepository.findByUsernameAndAccount(username, loanAccount).orElseThrow(() -> new RuntimeException("Loan account not found"));
        var list = loan.repayments;
        return Optional.ofNullable(list).map(repaymentEntities -> repaymentEntities.stream().map(repaymentEntity -> {
            var repayment = new Repayment();
            repayment.setId(repaymentEntity.id);
            repayment.setAmount(repaymentEntity.amount);
            repayment.setDueDate(repaymentEntity.due_date.toLocalDate());
            repayment.setStatus(RepaymentStatus.fromValue(repaymentEntity.status));
            repayment.setCreatedDate(repaymentEntity.createdDate.toLocalDateTime());
            return repayment;
        }).filter(repayment -> {
            if (repaymentStatus != null && RepaymentStatus.valueOf(repaymentStatus) != null) {
                return repayment.getStatus().equals(RepaymentStatus.valueOf(repaymentStatus));
            } else {
                return true;
            }
        }).toList()).orElse(new ArrayList<>());
    }

    public RepaymentEntity getRepaymentEntity(Repayment repayment) {
        var repaymentEntity = new RepaymentEntity();
        repaymentEntity.id = repayment.getId();
        repaymentEntity.status = repayment.getStatus().getValue();
        repaymentEntity.due_date = Date.valueOf(repayment.getDueDate());
        repaymentEntity.amount = repayment.getAmount();
        repaymentEntity.createdDate = Timestamp.valueOf(LocalDateTime.now());
        return repaymentEntity;
    }

}


