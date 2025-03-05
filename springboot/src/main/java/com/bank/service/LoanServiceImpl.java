package com.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bank.model.BankAccount;
import com.bank.model.Loan;
import com.bank.repository.BankAccountRepository;
import com.bank.repository.LoanRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
public class LoanServiceImpl {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public Loan createLoan(int userId, int bankAccountId, BigDecimal totalPaymentAmount, BigDecimal downPaymentAmount, int tenureYears, String loanType) {
     
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(bankAccountId);

        if (!optionalBankAccount.isPresent()) {
            throw new IllegalArgumentException("No bank account found for the provided account ID.");
        }

        BankAccount bankAccount = optionalBankAccount.get();

        BigDecimal loanAmount = totalPaymentAmount.subtract(downPaymentAmount);
        double interestRate = calculateInterestRate(tenureYears); // Assuming fixed rate based on tenure
        double emi = calculateEMI(loanAmount, interestRate, tenureYears);

        Loan loan = new Loan();
        loan.setUser(bankAccount.getUser());
        loan.setBankAccountId(bankAccountId);
        loan.setTotalPayment(totalPaymentAmount);
        loan.setDownPayment(downPaymentAmount);
        loan.setInterestRate(BigDecimal.valueOf(interestRate));
        loan.setTenureYears(tenureYears);
        loan.setEmiAmount(BigDecimal.valueOf(emi));
        loan.setSanctionDate(new Date());
        loan.setFullyPaid(false);  
        loan.setPaidInMonths(0);  
        loan.setPayableMonth(tenureYears * 12);

        loan.setLoanType(loanType);  
        return loanRepository.save(loan);    
    }

    private double calculateInterestRate(int tenureYears) {
        if (tenureYears >= 5 && tenureYears <= 15) {
            return 8.0 + (tenureYears - 5) * 1.0;
        } else if (tenureYears >= 2 && tenureYears <= 8) {
            return 7.0 + (tenureYears - 2) * 0.5;
        } else if (tenureYears >= 2 && tenureYears <= 8) {
            return 6.0 + (tenureYears - 2) * 0.5;
        } else if (tenureYears >= 1 && tenureYears <= 5) {
            return 10.0 + (tenureYears - 1) * 1.0;
        } else {
            throw new IllegalArgumentException("Invalid tenure. Tenure must be within the supported ranges.");
        }
    }

    private double calculateEMI(BigDecimal loanAmount, double interestRate, int tenureYears) {
        double monthlyInterestRate = interestRate / 12 / 100;
        int numberOfPayments = tenureYears * 12;
        return (loanAmount.doubleValue() * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfPayments)) /
               (Math.pow(1 + monthlyInterestRate, numberOfPayments) - 1);
    }

    private int getLoanTypeChoice(String loanType) {
        switch (loanType.toLowerCase()) {
            case "personal":
                return 1;
            case "home":
                return 2;
            case "car":
                return 3;
            case "education":
                return 4;
            default:
                throw new IllegalArgumentException("Invalid loan type.");
        }
    }
}

