package com.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.dto.BudgetRequestDto;
import com.bank.model.Budget;
import com.bank.service.JwtUserDetailsService;



@RestController
@RequestMapping("/netbanking/process")

public class BudgetController {

	 @Autowired
	 private JwtUserDetailsService userDetailsService;
	
	 @PostMapping("/calculate")
	    public ResponseEntity<Budget> calculateBudget(@RequestBody BudgetRequestDto requestDto) {
	        Budget budget = userDetailsService.calculateBudget(requestDto);
	        return ResponseEntity.ok(budget);
	    }
}
