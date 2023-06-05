package com.miniaspire.loan.repository;

import com.miniaspire.loan.repository.entity.LoanEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ILoanRepository extends CrudRepository<LoanEntity, Integer> {
    Optional<LoanEntity> findByAccount(String account);

    @Query(value = "SELECT * from Loan l where l.loginId =:username AND l.account =:account ", nativeQuery = true)
    Optional<LoanEntity> findByUsernameAndAccount(@Param("username") String username,
                                                  @Param("account") String account);

    List<LoanEntity> findByLoginId(String username);
}
