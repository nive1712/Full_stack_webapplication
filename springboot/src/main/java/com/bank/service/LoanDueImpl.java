package com.bank.service;

import com.bank.dto.PayLoanDTO;
import com.bank.model.BankAccount;
import com.bank.model.Loan;
import com.bank.repository.BankAccountRepository;
import com.bank.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class LoanDueImpl {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Transactional
    public void payLoanDue(PayLoanDTO payLoanDTO) {
        int accountId = payLoanDTO.getBankAccountId();

        Optional<BankAccount> optionalAccount = bankAccountRepository.findByAccountId(accountId);
        BankAccount account = optionalAccount.orElseThrow(() -> 
            new IllegalArgumentException("Invalid account number.")
        );

        Optional<Loan> optionalLoan = loanRepository.findByBankAccountId(accountId);
        Loan loan = optionalLoan.orElseThrow(() -> 
            new IllegalArgumentException("No loan found for this account.")
        );
        if (loan.isFullyPaid()) {
            throw new IllegalArgumentException("You have already paid off the loan.");
        }

        int totalLoanMonths = payLoanDTO.getTenureYears() * 12;
        
       

        if (loan.getPaidInMonths() >= totalLoanMonths) {
            throw new IllegalArgumentException("Loan tenure completed. No further EMIs to pay.");
        }

        BigDecimal emiAmount = loan.getEmiAmount();
        BigDecimal balance = account.getBalance();

        if (balance.compareTo(emiAmount) < 0) {
        	
            throw new IllegalArgumentException("Insufficient balance to pay the EMI.");
        }

        balance = balance.subtract(emiAmount);
        account.setBalance(balance);
        loan.setPaidInMonths(loan.getPaidInMonths() + 1);

        if (loan.getPaidInMonths() == totalLoanMonths) {
            loan.setFullyPaid(true);
        }

        bankAccountRepository.save(account);
        loanRepository.save(loan);
    }
}
