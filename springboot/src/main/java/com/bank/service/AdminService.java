package com.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.dto.CardBlockRequestDto;
import com.bank.model.BankAccount;
import com.bank.model.CardBlockStatus;
import com.bank.repository.BankAccountRepository;
import com.bank.repository.CardBlockStatusRepository;

@Service
public class AdminService {

    @Autowired
    private CardBlockStatusRepository cardBlockStatusRepository;
    
    @Autowired 
    private BankAccountRepository bankAccountRepository;

    public boolean approveCardBlockStatus(CardBlockRequestDto requestDto) {
     
        Integer cardBlockStatusId = getCardBlockStatusIdFromDto(requestDto);

        System.out.println(cardBlockStatusId );
        CardBlockStatus cardBlockStatus = cardBlockStatusRepository.findById(cardBlockStatusId).orElse(null);
        System.out.println(cardBlockStatus);

        if (cardBlockStatus != null && "Pending".equals(cardBlockStatus.getStatus())) {
            cardBlockStatus.setStatus("Approvd");
            cardBlockStatusRepository.save(cardBlockStatus);
            return true;
        }
        return false;
    }
    
   


    public boolean rejectCardBlockStatus(CardBlockRequestDto requestDto) {
        Integer cardBlockStatusId = getCardBlockStatusIdFromDto(requestDto);
        CardBlockStatus cardBlockStatus = cardBlockStatusRepository.findById(cardBlockStatusId).orElse(null);

        if (cardBlockStatus != null && "Pending".equals(cardBlockStatus.getStatus())) {
            cardBlockStatus.setStatus("Rejected");
            cardBlockStatusRepository.save(cardBlockStatus);
            return true;
        }
        return false;
    }
    private Integer getCardBlockStatusIdFromDto(CardBlockRequestDto requestDto) {
        return null; 
    }
    
    public List<CardBlockStatus> getPendingCardBlockStatuses() {
        return cardBlockStatusRepository.findByStatus("Pending");
    }
    public String getAccountNumberById(int accountId) {
        return bankAccountRepository.findAccountNumberById(accountId);
    }

}
