package com.bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bank.model.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {

	Optional<Loan> findByBankAccountId(int accountId);
}
