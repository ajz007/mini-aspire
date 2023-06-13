package com.miniaspire.loan.controller;

import com.miniaspire.loan.dto.Loan;
import com.miniaspire.loan.dto.LoanCreateRequest;
import com.miniaspire.loan.dto.LoanCreateRes;
import com.miniaspire.loan.dto.Repayment;
import com.miniaspire.loan.exceptions.UnAuthorisedAccessException;
import com.miniaspire.loan.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loan")
public class LoanRestController {

    public final LoanService loanService;
    private static final String USER_NAME = "x-user_name";
    private static final String USER_ROLE = "x-user_role";
    private static final String SERVICE_ROLE = "service_role";
    private static final String EX_MSG = "Please login to continue";

    LoanRestController(LoanService loanService) {
        this.loanService = loanService;
    }

    @Operation(summary = "Get all the loans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Loan[].class)) }),
            @ApiResponse(responseCode = "403", description = "Please login to continue",
                    content = @Content)})
    @GetMapping("")
    public ResponseEntity<List<Loan>> getLoans(@RequestHeader Map<String, String> headers) {
        if (headers.get(USER_NAME) == null || headers.get(USER_ROLE) == null) {
            throw new UnAuthorisedAccessException(EX_MSG);
        }
        var list = loanService.getUserLoans(headers.get(USER_NAME),
                headers.get(USER_ROLE));
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "Get details of a Loan Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Loan.class)) }),
            @ApiResponse(responseCode = "400", description = "Loan account not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Please login to continue",
                    content = @Content)})
    @GetMapping("/{loanAccount}")
    public ResponseEntity<Loan> getLoan(@PathVariable String loanAccount,
                                        @RequestHeader Map<String, String> headers) {
        if (headers.get(USER_NAME) == null || headers.get(USER_ROLE) == null) {
            throw new UnAuthorisedAccessException(EX_MSG);
        }
        return ResponseEntity.ok().body(loanService.getLoan(headers
                .get(USER_NAME), headers.get(USER_ROLE), loanAccount));
    }

    @Operation(summary = "Update the status of the loan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan Status updated with APPROVED",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "Loan account not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Please login to continue",
                    content = @Content)})
    @PutMapping("/{loanAccount}")
    public ResponseEntity<String> updateLoanStatus(@PathVariable String loanAccount,
                                                   @RequestParam String loanStatus,
                                                   @RequestHeader Map<String, String> headers) {
        if (headers.get(USER_NAME) == null || headers.get(USER_ROLE) == null) {
            throw new UnAuthorisedAccessException(EX_MSG);
        }
        loanService.updateLoanStatus(headers.get(USER_ROLE), headers.get(SERVICE_ROLE), loanAccount, loanStatus);

        return ResponseEntity.ok("Loan Status updated with " + loanStatus);
    }

    @Operation(summary = "Get all loans for a user. Only allowed for ADMIN role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Loan[].class)) }),
            @ApiResponse(responseCode = "400", description = "You do not have sufficient access for this service",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Please login to continue",
                    content = @Content)})
    @GetMapping("/user/{loginId}")
    public ResponseEntity<List<Loan>> getUserLoans(@PathVariable String loginId,
                                                   @RequestHeader Map<String, String> headers) {
        if (headers.get(USER_NAME) == null || headers.get(USER_ROLE) == null) {
            throw new UnAuthorisedAccessException(EX_MSG);
        }
        if (!"ADMIN".equalsIgnoreCase(headers.get(USER_ROLE))) {
            throw new UnAuthorisedAccessException("You do not have sufficient access for this service");
        }
        return ResponseEntity.ok()
                .body(loanService
                        .getUserLoans(headers.get(USER_NAME), headers.get(USER_ROLE)));
    }

    @Operation(summary = "Create a loan. Only allowed for user role - USER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanCreateRes.class)) }),
            @ApiResponse(responseCode = "400", description = "You do not have sufficient access for this service",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Please login to continue",
                    content = @Content)})
    @PostMapping("")
    public ResponseEntity<LoanCreateRes> createLoan(@RequestBody LoanCreateRequest loanCreateRequest,
                                                    @RequestHeader Map<String, String> headers) {
        var loan = new Loan();
        loan.setAccount(loanCreateRequest.getAccount());
        loan.setLoanAmount(loanCreateRequest.getLoanAmount());
        loan.setTerm(loanCreateRequest.getTerm());
        loan.setLoginId(headers.get(USER_NAME));
        loan.setUserRole(headers.get(USER_ROLE));
        return new ResponseEntity<>(loanService.createLoan(loan), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all repayments for a loan account and and repayment status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Repayment[].class)) }),
            @ApiResponse(responseCode = "400", description = "Loan account not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Please login to continue",
                    content = @Content)})
    @GetMapping("repayments/{loanAccount}")
    public ResponseEntity<List<Repayment>> getRepayments(@PathVariable String loanAccount,
                                                         @RequestParam(required = false) String repaymentStatus,
                                                         @RequestHeader Map<String, String> headers) {

        return ResponseEntity.ok(loanService
                .getRepayments(headers.get(USER_NAME), headers.get(USER_ROLE), loanAccount, repaymentStatus));
    }

    @Operation(summary = "Update repayment details. Only allowed from role - SERVICE, ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "Unknown value for RepaymentStatus",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Please login to continue",
                    content = @Content)})
    @PutMapping("/repayments/{id}")
    public ResponseEntity<String> updateRepayment(@PathVariable String id,
                                                  @RequestBody Repayment repayment,
                                                  @RequestHeader Map<String, String> headers) {

        if (headers.get(USER_NAME) == null || headers.get(USER_ROLE) == null) {
            throw new UnAuthorisedAccessException(EX_MSG);
        }
        loanService.updateRepaymentStatus(headers.get(USER_NAME),
                headers.get(USER_ROLE), headers.get(SERVICE_ROLE), repayment);
        return ResponseEntity.ok("Repayment updated");
    }

}
