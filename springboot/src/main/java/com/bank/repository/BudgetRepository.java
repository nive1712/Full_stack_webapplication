package com.bank.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.model.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget,Integer> {
    
}
