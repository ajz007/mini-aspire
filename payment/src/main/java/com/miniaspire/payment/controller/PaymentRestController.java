package com.miniaspire.payment.controller;

import com.miniaspire.payment.dto.PaymentRequest;
import com.miniaspire.payment.dto.PaymentResponse;
import com.miniaspire.payment.exceptions.UnAuthorisedAccessException;
import com.miniaspire.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentRestController {

    private PaymentService paymentService;
    private static final String USER_NAME = "x-user_name";
    private static final String USER_ROLE = "x-user_role";

    public PaymentRestController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest,
                                                         @RequestHeader Map<String, String> headers) {
        if (headers.get(USER_NAME) == null && headers.get(USER_ROLE) == null
                || headers.get(USER_ROLE).equalsIgnoreCase("ADMIN")) {
            throw new UnAuthorisedAccessException("You are not authorised to view this or your session has expired.");
        }
        paymentService.createPayment(paymentRequest, headers.get(USER_NAME), headers.get(USER_ROLE));

        return new ResponseEntity<>(new PaymentResponse(
                paymentRequest.getAmount(),
                "Payment Success!"), HttpStatus.CREATED);
    }
}
