package com.bank.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.model.BankAccount;
import com.bank.model.CardBlockStatus;

@Repository
public interface CardBlockStatusRepository extends JpaRepository<CardBlockStatus, Integer> {

    Optional<CardBlockStatus> findByBankAccount(BankAccount bankAccount);
    List<CardBlockStatus> findByStatus(String status);
}
