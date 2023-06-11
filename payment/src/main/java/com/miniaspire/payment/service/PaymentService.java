package com.miniaspire.payment.service;

import com.miniaspire.payment.dto.PaymentRequest;
import com.miniaspire.payment.dto.Repayment;
import com.miniaspire.payment.dto.RepaymentStatus;
import com.miniaspire.payment.exceptions.InvalidInputException;
import com.miniaspire.payment.exceptions.UnAuthorisedAccessException;
import com.miniaspire.payment.repository.PaymentRepositoryManager;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class PaymentService {

    private static final String USER_NAME = "x-user_name";
    private static final String USER_ROLE = "x-user_role";
    private static final String SERVICE_ROLE = "true";

    private final PaymentRepositoryManager paymentRepositoryManager;
    private final RestTemplate restTemplate;

    public PaymentService(PaymentRepositoryManager paymentRepositoryManager,
                          RestTemplate restTemplate) {
        this.paymentRepositoryManager = paymentRepositoryManager;
        this.restTemplate = restTemplate;
    }

    public void createPayment(PaymentRequest paymentRequest, String username, String userRole) {

        if (userRole.equalsIgnoreCase("ADMIN")) {
            throw new UnAuthorisedAccessException("You do not have access to this service");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set(USER_NAME, username);
        headers.set(USER_ROLE, userRole);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        //get all repayments This also validates if the loan account belongs to user. if not throw exception -- should happen in LOAN
        var repayments = restTemplate
                .exchange("http://LOAN/loan/repayments/" + paymentRequest.getLoanAccount()+"?repaymentStatus=PENDING",
                        HttpMethod.GET, entity, Repayment[].class);

        if (Optional.ofNullable(repayments.getBody()).orElse(new Repayment[0]).length == 0) {
            throw new InvalidInputException("No payments pending!");
        }
        //get the next pending repayment from repayment list-- filter here
        var scheduledRepayment = getScheduledRepayment(repayments.getBody());

        //validate if the amount is greater or equal to the emi
        //validate amount should not be greater than all remaining amount
        validateAmount(repayments.getBody(), scheduledRepayment, paymentRequest);

        //update the payment (history). Entire payment is history
        executePayment(paymentRequest, scheduledRepayment, username);

        //update the specific repayment status (ensure a service in loan for this) -- LOAN
        scheduledRepayment.setStatus(RepaymentStatus.PAID); //TODO: create a deep clone
        updateRepaymentStatus(username, userRole, scheduledRepayment);

        //TODO: Find remaining balance and adjust, update the next repayment amount

        //check if all repayments are paid
        //Update loan status to paid
        updateLoanStatus(repayments.getBody(), paymentRequest, username, userRole);
    }


    private Repayment getScheduledRepayment(Repayment[] repaymentList) {

        return Stream.of(repaymentList)
                .min(Comparator.comparing(Repayment::getDueDate)).orElse(new Repayment());
    }

    private void validateAmount(Repayment[] repayments, Repayment scheduledRepayment,
                                PaymentRequest paymentRequest) {

        if (paymentRequest.getAmount().compareTo(scheduledRepayment.getAmount()) < 0) {
            throw new InvalidInputException("The amount should be greater than or equal to " + scheduledRepayment.getAmount());
        }
        var sum = Stream.of(repayments).map(Repayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (paymentRequest.getAmount().compareTo(sum) > 0) {
            throw new InvalidInputException("The amount should be equal to repayment");
        }
    }

    private void executePayment(PaymentRequest paymentRequest, Repayment repayment, String username) {
        paymentRequest.setPaidBy(username);
        paymentRequest.setRepaymentId(repayment.getId());
        paymentRepositoryManager.createPayment(paymentRequest);
    }

    /**
     * Updated the scheduled RepaymentStatus to PAID
     *
     * @param username
     * @param userRole
     * @param scheduledRepayment
     */
    private void updateRepaymentStatus(String username, String userRole, Repayment scheduledRepayment) {
      /*  HttpHeaders headers = new HttpHeaders();
        headers.set(USER_NAME, username);
        headers.set(USER_ROLE, userRole);
        headers.set(SERVICE_ROLE, "true");
        headers.setContentType(MediaType.APPLICATION_JSON);
        //HttpEntity<String> entity = new HttpEntity<>("body", headers);
        HttpEntity<Repayment> requestUpdate = new HttpEntity<>(scheduledRepayment, headers);*/

       /* restTemplate.put("http://LOAN/loan/repayments/" + scheduledRepayment.getId(),
                scheduledRepayment);*/
        restTemplate.put("http://LOAN/loan/repayments/" + scheduledRepayment.getId(),
                HttpMethod.PUT, Void.class, scheduledRepayment);
    }

    /**
     * Update the Loan status to CLOSED if all the repayments are PAID
     *
     * @param repayments
     * @param paymentRequest
     * @param username
     * @param userRole
     */
    private void updateLoanStatus(Repayment[] repayments, PaymentRequest paymentRequest, String username, String userRole) {

        if (Stream.of(repayments).map(Repayment::getStatus)
                .anyMatch(repaymentStatus -> repaymentStatus.equals(RepaymentStatus.PENDING))) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(USER_NAME, username);
        headers.set(USER_ROLE, userRole);
        headers.set(SERVICE_ROLE, "true");
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        restTemplate.put("http://LOAN/loan/" + paymentRequest.getLoanAccount() + "?loanStatus=" + "CLOSED", entity);
    }
}
