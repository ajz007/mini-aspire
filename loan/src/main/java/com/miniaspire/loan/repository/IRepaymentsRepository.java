package com.miniaspire.loan.repository;

import com.miniaspire.loan.repository.entity.RepaymentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepaymentsRepository extends CrudRepository<RepaymentEntity, Integer> {
}
