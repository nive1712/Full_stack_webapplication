package com.bank.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.model.BankAccount;
import java.util.List;


public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
	 Optional<BankAccount> findByUser_UserId(int userId);

	    @Query("SELECT b FROM BankAccount b WHERE b.user.id = :userId")
	    Optional<BankAccount> findByUserId(@Param("userId") int userId);

	    @Query("SELECT b FROM BankAccount b WHERE b.accountNumber = :accountNumber")
	    Optional<BankAccount> findByAccountNumber(String accountNumber);
	    
	    Optional<BankAccount> findByAccountId(int accountId);
	    
	    @Query("SELECT b.accountNumber FROM BankAccount b WHERE b.accountId = :accountId")
	    String findAccountNumberById(@Param("accountId") int accountId);
	    
}
