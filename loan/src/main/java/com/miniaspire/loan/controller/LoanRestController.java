package com.miniaspire.loan.controller;

import com.miniaspire.loan.entity.Loan;
import com.miniaspire.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/loans")
public class LoanRestController {

    @Autowired
    public LoanService loanService;

    LoanRestController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/loans")
    public ResponseEntity<List<Loan>> getLoans(){
        var list = loanService.getLoans();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/loans/{id}")
    public ResponseEntity<Loan> getLoans(@RequestParam String id){
        return null;
    }

    /*@PostMapping("/")
    public ResponseEntity<Loan> createLoan(){
        return null;
    }*/
}
