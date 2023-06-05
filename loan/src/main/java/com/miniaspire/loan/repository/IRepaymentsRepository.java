package com.miniaspire.loan.repository;

import com.miniaspire.loan.repository.entity.RepaymentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRepaymentsRepository extends CrudRepository<RepaymentEntity, Integer> {
    //List<RepaymentEntity> findByLoanId(int id);
}
