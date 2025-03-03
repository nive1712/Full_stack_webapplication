package com.bank.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.dto.DepositRequestDto;
import com.bank.jwt.JwtTokenUtil;
import com.bank.model.BankAccount;
import com.bank.model.CardBlockStatus;
import com.bank.model.User;
import com.bank.service.JwtUserDetailsService;


@RestController
@RequestMapping("/netbanking/process")
public class DepositController_Netbanking {

	    @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private JwtTokenUtil jwtTokenUtil;

	    @Autowired
	    private JwtUserDetailsService userDetailsService;

	
	@PostMapping("/deposit")
    public ResponseEntity<String> handleDeposit(@RequestBody DepositRequestDto depositRequest,
                                                @RequestHeader("Authorization") String token) {
      
        String jwtToken = token.substring(7);

        String username = jwtTokenUtil.extractUsername(jwtToken);
        
        System.out.println("Username from token: " + username);

        User user = userDetailsService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        if (!isAccountNumberValid(depositRequest.getAccountNumber())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid account number.");
        }

        String accountNumber = depositRequest.getAccountNumber(); 
        if (!isPinValid(username, accountNumber, depositRequest.getPin())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid account number or PIN.");
        }

        
        Optional<CardBlockStatus> cardBlockStatusOpt = userDetailsService.getCardBlockStatusByAccountNumber(depositRequest.getAccountNumber());
        if (cardBlockStatusOpt.isPresent() && "approved".equalsIgnoreCase(cardBlockStatusOpt.get().getStatus())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your card is blocked. You cannot make deposits.");
        }

        // Handle deposit
        boolean success = userDetailsService.handleDeposit(user.getUserId(), depositRequest.getAmount());
        if (success) {
            return ResponseEntity.ok("Netbanking Deposit successful!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Deposit failed.");
        }
    }
	  private boolean isAccountNumberValid(String accountNumber) {
	        return accountNumber != null && !accountNumber.isEmpty(); 
	    }

	    private boolean isPinValid(String username, String accountNumber, int pin) {
	        User user = userDetailsService.findByUsername(username);
	        if (user != null) {
	            for (BankAccount bankAccount : user.getBankAccounts()) {
	                if (bankAccount.getAccountNumber().equals(String.valueOf(accountNumber)) && bankAccount.getPin() == pin) {
	                    return true; 
	                }
	            }
	        }
	        return false; 
	    }

}
