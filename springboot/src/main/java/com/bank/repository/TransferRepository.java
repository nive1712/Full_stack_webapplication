package com.bank.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.model.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Integer> {
   
}
