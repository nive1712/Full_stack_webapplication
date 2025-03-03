package com.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.dto.LoanRequestDto;
import com.bank.jwt.JwtTokenUtil;
import com.bank.model.CardBlockStatus;
import com.bank.model.Loan;
import com.bank.service.JwtUserDetailsService;
import com.bank.service.LoanServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/loan")
public class LoanController {

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private LoanServiceImpl loanService;

    @PostMapping("/apply")
    public ResponseEntity<String> applyForLoan(@RequestBody LoanRequestDto requestDto,
                                               @RequestHeader("Authorization") String token) {
        try {
        
            String jwtToken = token.substring(7); 
            String username = jwtTokenUtil.extractUsername(jwtToken);
            int userId = userDetailsService.getUserIdByUsername(username);
            
            Optional<CardBlockStatus> cardBlockStatusOpt = userDetailsService.getCardBlockStatusByAccountNumber(requestDto.getAccountNumber());
            if (cardBlockStatusOpt.isPresent() && "approved".equalsIgnoreCase(cardBlockStatusOpt.get().getStatus())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your card is blocked. You cannot apply for a loan.");
            }


            int bankAccountId = requestDto.getBankAccountId();
            BigDecimal totalPaymentAmount = requestDto.getTotalPayment();
            BigDecimal downPaymentAmount = requestDto.getDownPayment();
            int tenureYears = requestDto.getTenureYears();
            String loanType = requestDto.getLoanType();
            if (totalPaymentAmount == null || downPaymentAmount == null) {
                throw new IllegalArgumentException("Total payment amount or down payment amount is null.");
            }

            Loan loan = loanService.createLoan(userId, bankAccountId, totalPaymentAmount, downPaymentAmount, tenureYears, loanType);

            return ResponseEntity.ok("Loan applied successfully. Loan ID: " + loan.getLoanId());
    

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to apply for loan: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
