package com.miniaspire.loan.repository;

import com.miniaspire.loan.repository.entity.LoanEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILoanRepository extends CrudRepository<LoanEntity, Integer> {
}
