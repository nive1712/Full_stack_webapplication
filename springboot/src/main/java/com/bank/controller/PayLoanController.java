package com.bank.controller;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bank.dto.PayLoanDTO;
import com.bank.model.CardBlockStatus;
import com.bank.service.JwtUserDetailsService;
import com.bank.service.LoanDueImpl;

@RestController
@RequestMapping("/api/loandue")
public class PayLoanController {
	
	@Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private LoanDueImpl loanService;

    @PostMapping("/pay")
    public ResponseEntity<String> payLoanDue(@RequestBody PayLoanDTO payLoanDTO) {
        try {
        	 Optional<CardBlockStatus> cardBlockStatusOpt = userDetailsService.getCardBlockStatusByAccountNumber(payLoanDTO.getAccountNumber());
             if (cardBlockStatusOpt.isPresent() && "approved".equalsIgnoreCase(cardBlockStatusOpt.get().getStatus())) {
                 return ResponseEntity.status(403).body("Your card is blocked. You cannot make EMI payments.");
             }
        	
            loanService.payLoanDue(payLoanDTO);
            
            return ResponseEntity.ok("EMI payment successful.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
