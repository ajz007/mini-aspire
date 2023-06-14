package com.miniaspire.loan.service;

import com.miniaspire.loan.dto.Loan;
import com.miniaspire.loan.exceptions.UnAuthorisedAccessException;
import com.miniaspire.loan.repository.ILoanRepository;
import com.miniaspire.loan.repository.LoanRepositoryManager;
import com.miniaspire.loan.repository.RepaymentsRepositoryManager;
import com.miniaspire.loan.repository.entity.LoanEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class LoanServiceTest {

    //data for customer
    private static final String USER_NAME_CUSTOMER = "testuser";
    private static final String USER_ROLE_CUSTOMER = "USER";

    //data for adin
    private static final String USER_NAME_ADMIN = "admin";
    private static final String USER_ROLE_ADMIN = "ADMIN";
    private static final String SERVICE_ROLE = "SERVICE_ROLE";
    private static final String SERVICE_ROLE_VALUE = "true";

    //loan account
    private static final String LOAN_ACCOUNT = "1001";
    private static final String NEW_LOAN_ACCOUNT = "1002";

    @Mock
    public ILoanRepository loanRepository;

    @InjectMocks
    private LoanRepositoryManager loanRepositoryManager;
    @InjectMocks
    private RepaymentsRepositoryManager repaymentsRepositoryManager;


    private RepaymentsManager repaymentsManager = new RepaymentsManager();

    private LoanService loanService  = null;

    @Before
    public void setup() {
        Mockito.when(loanRepository.findByAccount(LOAN_ACCOUNT))
                .thenReturn(Optional.of(getLoanEntity(LOAN_ACCOUNT,
                        BigDecimal.valueOf(10000), 3, USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER)));
        Mockito.when(loanRepository.findByAccount(NEW_LOAN_ACCOUNT))
                .thenReturn(Optional.empty());

        Mockito.when(loanRepository.findByUsernameAndAccount(USER_NAME_CUSTOMER,LOAN_ACCOUNT))
                .thenReturn(Optional.of(getLoanEntity(LOAN_ACCOUNT,
                        BigDecimal.valueOf(10000), 3, USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER)));

        var listOdLoanEntity = new ArrayList<LoanEntity>();
        listOdLoanEntity.add(getLoanEntity(LOAN_ACCOUNT,
                BigDecimal.valueOf(10000), 3, USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER));
        Mockito.when(loanRepository.findByLoginId(USER_NAME_CUSTOMER))
                .thenReturn(listOdLoanEntity);

        Mockito.when(loanRepository.save(getLoanEntity(NEW_LOAN_ACCOUNT,
                        BigDecimal.valueOf(10000), 3, USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER)))
                .thenReturn(getLoanEntity(NEW_LOAN_ACCOUNT,
                        BigDecimal.valueOf(10000), 3, USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER));

        Mockito.when(loanRepository.save(getLoanEntity(LOAN_ACCOUNT,
                        BigDecimal.valueOf(10000), 3, USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER)))
                .thenReturn(getLoanEntity(LOAN_ACCOUNT,
                        BigDecimal.valueOf(10000), 3, USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER));

        loanRepositoryManager = new LoanRepositoryManager(loanRepository);
        repaymentsRepositoryManager =
                new RepaymentsRepositoryManager(null, loanRepository);
        loanService =
                new LoanService(loanRepositoryManager,
                        repaymentsRepositoryManager, repaymentsManager);

    }

    @Test
    public void createLoanWithAdminThrowsUnAuthException() {
        Assert.assertThrows(UnAuthorisedAccessException.class,
                () -> loanService.createLoan(getLoan(LOAN_ACCOUNT,
                        BigDecimal.valueOf(10000), 3, USER_NAME_ADMIN, USER_ROLE_ADMIN)));
    }

    @Test
    public void createLoanSunnyDay() {
        var res = loanService.createLoan(getLoan(NEW_LOAN_ACCOUNT,
                        BigDecimal.valueOf(10000), 3, USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER));
        Assert.assertEquals("Loan created successfully", res.getMsg());
    }

    @Test
    public void getLoanSunnyDay() {
        var res = loanService.getLoan(USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER, LOAN_ACCOUNT);
        Assert.assertEquals(LOAN_ACCOUNT, res.getAccount());
        Assert.assertEquals(USER_NAME_CUSTOMER, res.getLoginId());
    }

    @Test
    public void getUserLoansSunnyDay() {
        var res = loanService.getUserLoans(USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER);
        Assert.assertEquals(1, res.size());
        Assert.assertEquals(LOAN_ACCOUNT, res.get(0).getAccount());
        Assert.assertEquals(USER_NAME_CUSTOMER, res.get(0).getLoginId());
    }

    @Test
    public void getRepaymentsSunnyDay() {
        var res = loanService.getRepayments(USER_NAME_CUSTOMER,
                USER_ROLE_CUSTOMER, LOAN_ACCOUNT, "");
        Assert.assertEquals(0, res.size());
    }

    @Test
    public void updateLoanStatus() {
        loanService.updateLoanStatus(USER_ROLE_ADMIN, SERVICE_ROLE_VALUE, LOAN_ACCOUNT, "APPROVED");
        //Mockito.verify((loanRepository), Mockito.atMost(2)).save(LoanEntity.class);

    }


    private Loan getLoan(String loanAccount, BigDecimal loanAmount, int loanTerm,
                         String userName, String userRole) {
        var loan = new Loan();
        loan.setAccount(loanAccount);
        loan.setLoanAmount(loanAmount);
        loan.setTerm(loanTerm);
        loan.setLoginId(userName);
        loan.setUserRole(userRole);
        return loan;
    }

    private LoanEntity getLoanEntity(String loanAccount, BigDecimal loanAmount, int loanTerm,
                                     String userName, String userRole) {
        var loan = new LoanEntity();
        loan.id = 1234;
        loan.account = loanAccount;
        loan.loanAmount = loanAmount;
        loan.term = loanTerm;
        loan.loginId = userName;
        loan.repayments = new HashSet<>();
        loan.createdDate = Timestamp.valueOf(LocalDateTime.now());
        return loan;
    }

}
