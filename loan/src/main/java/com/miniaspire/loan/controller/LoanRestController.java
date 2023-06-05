package com.miniaspire.loan.controller;

import com.miniaspire.loan.dto.Loan;
import com.miniaspire.loan.dto.LoanCreateRequest;
import com.miniaspire.loan.dto.LoanCreateRes;
import com.miniaspire.loan.dto.Repayment;
import com.miniaspire.loan.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loan")
public class LoanRestController {

    public final LoanService loanService;
    private final static String USER_NAME = "x-user_name";
    private final static String USER_ROLE = "x-user_role";

    LoanRestController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("")
    public ResponseEntity<List<Loan>> getLoans(@RequestHeader Map<String, String> headers) {
        var list = loanService.getUserLoans(headers.get(USER_NAME),
                headers.get(USER_ROLE));
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{loanAccount}")
    public ResponseEntity<Loan> getLoan(@PathVariable String loanAccount,
                                        @RequestHeader Map<String, String> headers) {
        return ResponseEntity.ok().body(loanService.getLoan(headers
                .get(USER_NAME), headers.get(USER_ROLE), loanAccount));
    }

    @PatchMapping("")
    public ResponseEntity<String> updateLoanStatus(@PathVariable String loanAccount,
                                                   @PathVariable String loanStatus,
                                                   @RequestHeader Map<String, String> headers) {
        loanService.updateLoanStatus(headers.get(USER_ROLE), loanAccount, loanStatus);

        return ResponseEntity.ok("Loan Status updated with " + loanStatus);
    }


    @GetMapping("/user/{loginId}")
    public ResponseEntity<List<Loan>> getUserLoans(@PathVariable String loginId,
                                                   @RequestHeader Map<String, String> headers) {
        if(loginId!=null && loginId.equalsIgnoreCase(headers.get(USER_NAME))) {
            throw new RuntimeException("Please check the if the login Id is correct or your session is still active");
        }
        return ResponseEntity.ok()
                .body(loanService
                        .getUserLoans(headers.get(USER_NAME), headers.get(USER_ROLE)));
    }


    @PostMapping("")
    public ResponseEntity<LoanCreateRes> createLoan(@RequestBody LoanCreateRequest loanCreateRequest,
                                                    @RequestHeader Map<String, String> headers) {
        var loan = new Loan();
        loan.setAccount(loanCreateRequest.getAccount());
        loan.setLoanAmount(loanCreateRequest.getLoanAmount());
        loan.setTerm(loanCreateRequest.getTerm());
        loan.setLoginId(headers.get(USER_NAME));
        loan.setUserRole(headers.get(USER_ROLE));
        return new ResponseEntity<LoanCreateRes>(loanService.createLoan(loan), HttpStatus.CREATED);
    }

    @GetMapping("repayments")
    public ResponseEntity<List<Repayment>> getRepayments(@RequestParam String loanAccount,
                                                         @RequestHeader Map<String, String> headers) {

        return ResponseEntity.ok(loanService
                .getRepayments(headers.get(USER_NAME), headers.get(USER_ROLE), loanAccount));
    }

}
