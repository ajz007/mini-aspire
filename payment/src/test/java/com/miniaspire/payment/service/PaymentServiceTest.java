package com.miniaspire.payment.service;

import com.miniaspire.payment.dto.Loan;
import com.miniaspire.payment.dto.LoanStatus;
import com.miniaspire.payment.dto.PaymentRequest;
import com.miniaspire.payment.dto.Repayment;
import com.miniaspire.payment.repository.IPaymentRepository;
import com.miniaspire.payment.repository.PaymentRepositoryManager;
import com.miniaspire.payment.repository.entity.PaymentEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class PaymentServiceTest {

    private static final String USER_NAME_HEADER = "x-user_name";
    private static final String USER_ROLE_HEADER = "x-user_role";
    private static final String SERVICE_ROLE = "service_role";


    //data for customer
    private static final String USER_NAME_CUSTOMER = "testuser";
    private static final String USER_ROLE_CUSTOMER = "USER";

    //data for admin
    private static final String USER_NAME_ADMIN = "admin";
    private static final String USER_ROLE_ADMIN = "ADMIN";

    //Account
    private static final Integer LOAN_ACCOUNT = 2000;
    private static final Integer REPAYMENT_ID = 11;
    private static final Integer REPAYMENT_AMOUNT = 10000;

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private IPaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;
    @InjectMocks
    private PaymentRepositoryManager paymentRepositoryManager;

    @Before
    public void before() throws Exception {

        paymentRepositoryManager = new PaymentRepositoryManager(paymentRepository);
        paymentService = new PaymentService(paymentRepositoryManager, restTemplate);


        Mockito.when(restTemplate
                .exchange(Mockito.matches("http://LOAN/loan/" + LOAN_ACCOUNT),
                        Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(Loan.class))).thenReturn(new ResponseEntity<>(getLoan(), HttpStatus.OK));


        Mockito.when(restTemplate
                        .exchange(Mockito.eq("http://LOAN/loan/repayments/" + LOAN_ACCOUNT + "?repaymentStatus=PENDING"),
                                Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.same(Repayment[].class)))
                .thenReturn(new ResponseEntity<>(getRepayments().toArray(Repayment[]::new), HttpStatus.OK));

        //
        Mockito.when(paymentRepository.findByRepaymentId(REPAYMENT_ID)).thenReturn(Optional.ofNullable(null));

        Mockito.when(restTemplate.exchange(Mockito.eq("http://LOAN/loan/repayments/" + REPAYMENT_ID),
                Mockito.eq(HttpMethod.PUT), Mockito.any(), Mockito.same(String.class))).thenReturn(new ResponseEntity<>(new String(), HttpStatus.OK));

        Mockito.when(restTemplate.exchange(Mockito.eq("http://LOAN/loan/" + LOAN_ACCOUNT + "?loanStatus=" + "CLOSED"),
                Mockito.eq(HttpMethod.PUT), Mockito.any(), Mockito.same(String.class))).thenReturn(new ResponseEntity<>(new String(), HttpStatus.OK));
    }

    @Test
    public void createPaymentSunnyDay() throws Exception {
        paymentService.createPayment(getPaymentRequest(LOAN_ACCOUNT.toString()), USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER);
        Mockito.verify(paymentRepository, Mockito.atLeastOnce());
    }

    private PaymentRequest getPaymentRequest(String loanAccount) {
        var paymentRequest = new PaymentRequest();
        paymentRequest.setLoanAccount(loanAccount);
        paymentRequest.setRepaymentId(REPAYMENT_ID);
        paymentRequest.setAmount(BigDecimal.valueOf(REPAYMENT_AMOUNT));
        return paymentRequest;
    }

    public Loan getLoan() {
        var loan = new Loan();
        loan.setStatus(LoanStatus.APPROVED);
        return loan;
    }

    public List<Repayment> getRepayments() {
        var repayment = new Repayment();
        repayment.setAmount(BigDecimal.valueOf(REPAYMENT_AMOUNT));
        repayment.setId(REPAYMENT_ID);
        repayment.setDueDate(LocalDate.now());
        repayment.setCreatedDate(LocalDateTime.now());
        return List.of(repayment);
    }

    public Repayment getRepayment() {
        var repayment = new Repayment();

        return repayment;
    }

    public PaymentEntity getPaymentEntity() {
        var paymentEntity = new PaymentEntity();
        paymentEntity.id = 1;
        paymentEntity.repaymentId = 2;
        paymentEntity.loanAccount = "23";

        return paymentEntity;
    }

}
