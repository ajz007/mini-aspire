package com.miniaspire.payment.repository;

import com.miniaspire.payment.repository.entity.PaymentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface IPaymentRepository extends CrudRepository<PaymentEntity, Integer> {

    Optional<PaymentEntity> findByRepaymentId(Integer repaymentId);
}
