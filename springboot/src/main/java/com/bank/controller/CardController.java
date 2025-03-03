package com.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.dto.CardBlockRequestDto;
import com.bank.dto.CardUnblockRequestDto;
import com.bank.jwt.JwtTokenUtil;
import com.bank.model.CardBlockStatus;
import com.bank.model.User;
import com.bank.service.AdminService;
import com.bank.service.JwtUserDetailsService;

@RequestMapping("/api/cards")

@RestController
public class CardController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    

    @Autowired
    private JwtUserDetailsService userDetailsService;
    
   
    @PostMapping("/block")
    public ResponseEntity<String> blockCard(@RequestBody CardBlockRequestDto requestDto,
                                            @RequestHeader("Authorization") String token) {
      
        String jwtToken = token.substring(7);
        String username = jwtTokenUtil.extractUsername(jwtToken);
        
        System.out.println("Username from token: " + username);

        User user = userDetailsService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        boolean success = userDetailsService.blockCard(user.getUserId(), 
                requestDto.getAccountNumber(), 
                requestDto.getPin(), 
                requestDto.getReason());

        if (success) {
            return ResponseEntity.ok("Card blocked successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to block card.");
        }
    }

    @PostMapping("/unblock")
    public ResponseEntity<String> unblockCard(@RequestBody CardUnblockRequestDto requestDto,
                                              @RequestHeader("Authorization") String token) {
     
        System.out.println("Received unblock request for account number: " + requestDto.getAccountNumber());
        System.out.println("Received pin: " + requestDto.getPin());

        String jwtToken = token.substring(7);  
        System.out.println("Extracted JWT token: " + jwtToken);

        String username = jwtTokenUtil.extractUsername(jwtToken);
        System.out.println("Username extracted from token: " + username);

        User user = userDetailsService.findByUsername(username);
        if (user == null) {
            System.out.println("User not found for username: " + username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        int userId = user.getUserId();  
        System.out.println("User ID: " + userId);

        System.out.println("Validating account number and pin for user ID: " + userId);
        boolean success = userDetailsService.unblockCard(userId, requestDto.getAccountNumber(), requestDto.getPin());
        System.out.println("Unblock card result: " + success);

        
        if (success) {
            System.out.println("Card successfully unblocked for account number: " + requestDto.getAccountNumber());
            return ResponseEntity.ok("Card unblocked successfully.");
        } else {
            System.out.println("Failed to unblock card for account number: " + requestDto.getAccountNumber());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to unblock card.");
        }
    }
}
