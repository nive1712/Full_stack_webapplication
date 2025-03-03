package com.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bank.dto.CardBlockRequestDto;
import com.bank.jwt.JwtTokenUtil;
import com.bank.model.BankAccount;
import com.bank.model.CardBlockStatus;
import com.bank.model.User;
import com.bank.repository.BankAccountRepository;
import com.bank.repository.CardBlockStatusRepository;
import com.bank.service.AdminService;
import com.bank.service.JwtUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/admin/card-block")
public class AdminController {

    @Autowired
    private CardBlockStatusRepository cardBlockStatusRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private AdminService adminservice;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("/approve")
    public ResponseEntity<String> approveCardBlockStatus(
        @RequestBody CardBlockRequestDto requestDto,
        @RequestHeader("Authorization") String token) {

        String jwtToken = token.substring(7);
        String username = jwtTokenUtil.extractUsername(jwtToken);
        System.out.println("approve username:" + username);
        User user = userDetailsService.findByUsername(username);

        System.out.println("user 1:" + user);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(requestDto.getAccountNumber())
                .orElse(null);

        if (bankAccount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bank account not found.");
        }
        CardBlockStatus cardBlockStatus = cardBlockStatusRepository.findByBankAccount(bankAccount)
                .orElse(null);

        if (cardBlockStatus == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card block status not found.");
        }
        
        cardBlockStatus.setStatus("Approved");
        cardBlockStatus.setCardBlocked(true);
        cardBlockStatusRepository.save(cardBlockStatus);

        return ResponseEntity.ok("Card block status approved.");
    }

    @PostMapping("/reject")
    public ResponseEntity<String> rejectCardBlockStatus(
        @RequestBody CardBlockRequestDto requestDto,
        @RequestHeader("Authorization") String token) {

        String jwtToken = token.substring(7); 
        String username = jwtTokenUtil.extractUsername(jwtToken);
        System.out.println("username:" + username);
        User user = userDetailsService.findByUsername(username);
        System.out.println("user:" + user);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(requestDto.getAccountNumber())
                .orElse(null);

        if (bankAccount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bank account not found.");
        }
        CardBlockStatus cardBlockStatus = cardBlockStatusRepository.findByBankAccount(bankAccount)
                .orElse(null);

        if (cardBlockStatus == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card block status not found.");
        }
        
        if ("Pending".equals(cardBlockStatus.getStatus())) {
            cardBlockStatus.setCardBlocked(false);
        }

        cardBlockStatus.setStatus("Rejected");
        cardBlockStatus.setCardBlocked(false);
        cardBlockStatusRepository.save(cardBlockStatus);

        return ResponseEntity.ok("Card block status rejected.");
    }
    
    

    @GetMapping("/pending")
    public List<CardBlockStatus> getPendingCardBlockStatuses() {
        return adminservice.getPendingCardBlockStatuses();
    }}
    

