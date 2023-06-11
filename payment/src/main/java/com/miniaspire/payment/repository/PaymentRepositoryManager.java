package com.miniaspire.payment.repository;

import com.miniaspire.payment.dto.PaymentRequest;
import com.miniaspire.payment.exceptions.InvalidInputException;
import com.miniaspire.payment.exceptions.TechnicalUnExpectedException;
import com.miniaspire.payment.repository.entity.PaymentEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class PaymentRepositoryManager {

    private final IPaymentRepository paymentRepository;

    public PaymentRepositoryManager(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void createPayment(PaymentRequest paymentRequest) {
        //validate if this repayment id already paid
        if(paymentRepository.findByRepaymentId(paymentRequest.getRepaymentId()).isPresent()) {
            throw new InvalidInputException("Payment already done for the scheduled repayment. Reach out to customer care if the problem persists.");
        }
        try {
            paymentRepository.save(getPaymentEntity(paymentRequest));
        } catch (Exception e) {
            throw new TechnicalUnExpectedException("Payment failed!");
        }
    }

    private PaymentEntity getPaymentEntity(PaymentRequest paymentRequest) {
        var paymentEntity = new PaymentEntity();
        paymentEntity.loanAccount = paymentRequest.getLoanAccount();
        paymentEntity.repaymentId = paymentRequest.getRepaymentId();
        paymentEntity.amount = paymentRequest.getAmount();
        paymentEntity.currency = "USD";
        paymentEntity.paidBy = paymentRequest.getPaidBy();
        paymentEntity.paymentDate = Timestamp.valueOf(LocalDateTime.now());
        paymentEntity.createdDate = Timestamp.valueOf(LocalDateTime.now());
        return paymentEntity;
    }
}
