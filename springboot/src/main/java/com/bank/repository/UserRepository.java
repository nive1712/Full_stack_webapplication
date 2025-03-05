package com.bank.repository;

import com.bank.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> { // Use Integer instead of Long
    User findByUsername(String username);
    
}
