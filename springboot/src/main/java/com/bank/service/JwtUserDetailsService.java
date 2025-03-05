package com.bank.service;

import com.bank.dto.BudgetRequestDto;
import com.bank.dto.LoanRequestDto;
import com.bank.model.BankAccount;
import com.bank.model.Budget;
import com.bank.model.CardBlockStatus;
import com.bank.model.Deposit;
import com.bank.model.Loan;
import com.bank.model.TransactionHistory;
import com.bank.model.Transfer;
import com.bank.model.User;
import com.bank.model.Withdraw;
import com.bank.repository.BankAccountRepository;
import com.bank.repository.BudgetRepository;
import com.bank.repository.CardBlockStatusRepository;
import com.bank.repository.DepositRepository;
import com.bank.repository.LoanRepository;
import com.bank.repository.TransactionHistoryRepository;
import com.bank.repository.TransferRepository;
import com.bank.repository.UserRepository;
import com.bank.repository.WithdrawRepository;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;


@Service
public class JwtUserDetailsService implements UserDetailsService {

    public JwtUserDetailsService(PasswordEncoder passwordEncoder) {
	
		this.passwordEncoder = passwordEncoder;
	}
	@Autowired
    private UserRepository userRepository;

    
    @Autowired
    private BankAccountRepository bankAccountRepository;
    
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;
    
    @Autowired
    private DepositRepository depositRepository;
    
    @Autowired
    private BudgetRepository budgetRepository;
    
    
    
    @Autowired
    private CardBlockStatusRepository cardBlockStatusRepository;
    @Autowired
    private WithdrawRepository withdrawRepository;
    
    @Autowired 
    private TransferRepository transferRepository;
    
  
    @Autowired
    private LoanRepository loanRepository;
    
 
    private PasswordEncoder passwordEncoder;

    private SecureRandom secureRandom = new SecureRandom();
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        System.out.println("userDetails");
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),Collections.singleton(new SimpleGrantedAuthority("ROLE_"+user.getRole())));

        
    }
    
    
    public int registerUser(User user, String password) {
       
        
        user.setPassword(passwordEncoder.encode(password));
        user = userRepository.save(user);

        if (user != null) {
            createBankAccount(user);
            return user.getUserId();
        }
        return -1;
    }
    
    public String getAccountNumberByUserId(int userId) {
        BankAccount bankAccount = bankAccountRepository.findByUserId(userId)
                .orElse(null);
        if (bankAccount != null) {
            return bankAccount.getAccountNumber();
        }
        return null;
    }

    private void createBankAccount(User user) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setUser(user);
        bankAccount.setAccountNumber(generateAccountNumber());
        
        bankAccount.setPin(secureRandom.nextInt(10000));
        bankAccount.setMfaPin(1111 + user.getUserId());
        bankAccount.setBalance(BigDecimal.valueOf(500));
        bankAccount.setInitialBalance(BigDecimal.valueOf(500));
        
        bankAccount = bankAccountRepository.save(bankAccount);

        if (user.getBankAccounts() == null) {
            user.setBankAccounts(new HashSet<>());
        }
        user.getBankAccounts().add(bankAccount);
        String email = user.getUsername() + "@oksbi";
        user.setEmail(email);
        user.setStatus("ACTIVE");
        userRepository.save(user);  
    }

    private String generateAccountNumber() {
       
        return "ACC" + secureRandom.nextInt(10000000);
    }

   
    public User save(User user) {
    	user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
       
        return userRepository.save(user);
    }

    public User saveAdmin(User user) {
        user.setRole("ADMIN");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user); 
    }

   
    public User findByUsername(String username) {
        return userRepository.findByUsername(username); 
    }
    
    public boolean withdraw(int userId, BigDecimal amount) {
       
        BankAccount bankAccount = bankAccountRepository.findByUser_UserId(userId)
                .orElse(null);

        if (bankAccount == null || bankAccount.getBalance().compareTo(amount) < 0) {
            return false;
        }
        BigDecimal newBalance = bankAccount.getBalance().subtract(amount);
        bankAccount.setBalance(newBalance);
        bankAccountRepository.save(bankAccount);

        Withdraw withdraw = new Withdraw();
        withdraw.setBankAccount(bankAccount);
        withdraw.setAmount(amount);
        withdraw.setWithdrawDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));        withdrawRepository.save(withdraw);

        TransactionHistory withdrawalTransaction = new TransactionHistory();
        withdrawalTransaction.setBankAccount(bankAccount);
        withdrawalTransaction.setAmount(amount);
        withdrawalTransaction.setDate(LocalDate.now());
        withdrawalTransaction.setDescription("Withdrawal transaction");
        withdrawalTransaction.setType("ATM Withdrawal");
        transactionHistoryRepository.save(withdrawalTransaction);

        return true;
    }

    
    public boolean handleDeposit(int userId, BigDecimal amount) {
        
        BankAccount bankAccount = bankAccountRepository.findByUser_UserId(userId)
                .orElse(null);

        if (bankAccount == null) {
            return false;
        }
        BigDecimal newBalance = bankAccount.getBalance().add(amount);
        bankAccount.setBalance(newBalance);
        bankAccountRepository.save(bankAccount); 
        Deposit deposit = new Deposit();
        deposit.setBankAccount(bankAccount);
        deposit.setAmount(amount);
        deposit.setDepositDate(LocalDate.now());
        depositRepository.save(deposit);

        TransactionHistory depositTransaction = new TransactionHistory();
        depositTransaction.setBankAccount(bankAccount);
        depositTransaction.setAmount(amount);
        depositTransaction.setDate(LocalDate.now());
        depositTransaction.setDescription("Deposit transaction");
        depositTransaction.setType("Netbanking Deposit");
        transactionHistoryRepository.save(depositTransaction);

        return true;
    }
    
    
    public boolean atmDeposit(int userId, BigDecimal amount) {
        BankAccount bankAccount = bankAccountRepository.findByUser_UserId(userId)
                .orElse(null);

        if (bankAccount == null) {
            return false;
        }
        BigDecimal newBalance = bankAccount.getBalance().add(amount);
        bankAccount.setBalance(newBalance);
        bankAccountRepository.save(bankAccount); 
        Deposit deposit = new Deposit();
        deposit.setBankAccount(bankAccount);
        deposit.setAmount(amount);
        deposit.setDepositDate(LocalDate.now());
        depositRepository.save(deposit);

        TransactionHistory depositTransaction = new TransactionHistory();
        depositTransaction.setBankAccount(bankAccount);
        depositTransaction.setAmount(amount);
        depositTransaction.setDate(LocalDate.now());
        depositTransaction.setDescription("Deposit transaction");
        depositTransaction.setType("Atm Deposit");
        transactionHistoryRepository.save(depositTransaction);

        return true;
    }
    
    
    public boolean atmTransfer(String senderAccountNumber, String recipientAccountNumber, BigDecimal transferAmount) {

        BankAccount senderAccount = bankAccountRepository.findByAccountNumber(senderAccountNumber)
                .orElse(null);
        BankAccount recipientAccount = bankAccountRepository.findByAccountNumber(recipientAccountNumber)
                .orElse(null);

        System.out.println("Sender Account Number: " + senderAccountNumber);
        System.out.println("Recipient Account Number: " + recipientAccountNumber);

        if (senderAccount == null) {
            System.out.println("Sender account not found.");
            return false;
        }

        if (recipientAccount == null) {
            System.out.println("Recipient account not found.");
            return false;
        }

        
        if (senderAccount.getBalance().compareTo(transferAmount) < 0) {
            System.out.println("Insufficient balance.");
            return false;
        }

        BigDecimal newSenderBalance = senderAccount.getBalance().subtract(transferAmount);
        senderAccount.setBalance(newSenderBalance);
        bankAccountRepository.save(senderAccount);

        BigDecimal newRecipientBalance = recipientAccount.getBalance().add(transferAmount);
        recipientAccount.setBalance(newRecipientBalance);
        bankAccountRepository.save(recipientAccount);

        Transfer transfer = new Transfer();
        transfer.setSourceAccount(senderAccount);
        transfer.setTargetAccount(recipientAccount);
        transfer.setAmount(transferAmount);
        transfer.setTransferDate(LocalDateTime.now());
        transferRepository.save(transfer);

        TransactionHistory senderTransaction = new TransactionHistory();
        senderTransaction.setBankAccount(senderAccount);
        senderTransaction.setAmount(transferAmount.negate());
        senderTransaction.setDate(LocalDate.now());
        senderTransaction.setType("Atm Transfer");
        senderTransaction.setDescription("Transfer to account " + recipientAccount.getAccountNumber());
        transactionHistoryRepository.save(senderTransaction);
        
        TransactionHistory recipientTransaction = new TransactionHistory();
        recipientTransaction.setBankAccount(recipientAccount);
        recipientTransaction.setAmount(transferAmount);
        recipientTransaction.setDate(LocalDate.now());
        recipientTransaction.setType("Atm Transfer");
        recipientTransaction.setDescription("Transfer from account " + senderAccount.getAccountNumber());
        transactionHistoryRepository.save(recipientTransaction);

        System.out.println("Transfer successful. Amount: " + transferAmount);
        return true;
    }
    
    public boolean handleTransfer(String senderAccountNumber, String recipientAccountNumber, BigDecimal transferAmount) {

        BankAccount senderAccount = bankAccountRepository.findByAccountNumber(senderAccountNumber)
                .orElse(null);
        BankAccount recipientAccount = bankAccountRepository.findByAccountNumber(recipientAccountNumber)
                .orElse(null);

        System.out.println("Sender Account Number: " + senderAccountNumber);
        System.out.println("Recipient Account Number: " + recipientAccountNumber);

        if (senderAccount == null) {
            System.out.println("Sender account not found.");
            return false;
        }

        if (recipientAccount == null) {
            System.out.println("Recipient account not found.");
            return false;
        }
        if (senderAccount.getBalance().compareTo(transferAmount) < 0) {
            System.out.println("Insufficient balance.");
            return false;
        }
        BigDecimal newSenderBalance = senderAccount.getBalance().subtract(transferAmount);
        senderAccount.setBalance(newSenderBalance);
        bankAccountRepository.save(senderAccount);

        BigDecimal newRecipientBalance = recipientAccount.getBalance().add(transferAmount);
        recipientAccount.setBalance(newRecipientBalance);
        bankAccountRepository.save(recipientAccount);

        Transfer transfer = new Transfer();
        transfer.setSourceAccount(senderAccount);
        transfer.setTargetAccount(recipientAccount);
        transfer.setAmount(transferAmount);
        transfer.setTransferDate(LocalDateTime.now());
        transferRepository.save(transfer);

        TransactionHistory senderTransaction = new TransactionHistory();
        senderTransaction.setBankAccount(senderAccount);
        senderTransaction.setAmount(transferAmount.negate());
        senderTransaction.setDate(LocalDate.now());
        senderTransaction.setType("Netbanking Transfer");
        senderTransaction.setDescription("Transfer to account " + recipientAccount.getAccountNumber());
        transactionHistoryRepository.save(senderTransaction);

        TransactionHistory recipientTransaction = new TransactionHistory();
        recipientTransaction.setBankAccount(recipientAccount);
        recipientTransaction.setAmount(transferAmount);
        recipientTransaction.setDate(LocalDate.now());
        recipientTransaction.setType("Netbanking Transfer");
        recipientTransaction.setDescription("Transfer from account " + senderAccount.getAccountNumber());
        transactionHistoryRepository.save(recipientTransaction);

        System.out.println("Transfer successful. Amount: " + transferAmount);
        return true;
    }
    public boolean blockCard(int userId, String accountNumber, int pin, String reason) {
        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElse(null);
        System.out.println("Account Number: " +bankAccount);
        if (bankAccount == null || bankAccount.getPin() != pin) {
            return false;
        }
        System.out.println("Sender Account Number: " + accountNumber);
      

        CardBlockStatus cardBlockStatus = new CardBlockStatus();
        cardBlockStatus.setBankAccount(bankAccount);
        cardBlockStatus.setBlockDate(LocalDate.now());
        cardBlockStatus.setBlockReason(reason);
        cardBlockStatus.setCardBlocked(false);
        cardBlockStatus.setStatus("Pending");
       

        cardBlockStatusRepository.save(cardBlockStatus);

        return true;
    }
    
    public Optional<CardBlockStatus> getCardBlockStatusByAccountNumber(String accountNumber) {
        Optional<BankAccount> bankAccountOpt = bankAccountRepository.findByAccountNumber(accountNumber);
        if (bankAccountOpt.isPresent()) {
            return cardBlockStatusRepository.findByBankAccount(bankAccountOpt.get());
        }
        return Optional.empty();
    }

    public boolean unblockCard(int userId, String accountNumber, int pin) {
        System.out.println("Checking account: " + accountNumber + " for pin: " + pin);

        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElse(null);
        if (bankAccount == null) {
            System.out.println("Bank account not found for account number: " + accountNumber);
            return false;
        }

        if (bankAccount.getPin() != pin) {
            System.out.println("Incorrect PIN for account number: " + accountNumber);
            return false;
        }

        CardBlockStatus cardBlockStatus = cardBlockStatusRepository.findByBankAccount(bankAccount)
                .orElse(null);
        if (cardBlockStatus == null || !cardBlockStatus.isCardBlocked()) {
            System.out.println("Card is not blocked for account number: " + accountNumber);
            return false;
        }

        cardBlockStatus.setCardBlocked(false);
        cardBlockStatus.setUnblockDate(LocalDate.now());
        cardBlockStatus.setStatus("Unblocked");
        cardBlockStatusRepository.save(cardBlockStatus);

        System.out.println("Card successfully unblocked for account number: " + accountNumber);
        return true;
    }

    
    public Budget calculateBudget(BudgetRequestDto requestDto) {
        BigDecimal totalIncome = requestDto.getTotalIncome();
        BigDecimal totalExpenses = requestDto.getTotalExpenses();
        BigDecimal debtRepayment = requestDto.getDebtRepayment();

        BigDecimal budgetBalance = totalIncome.subtract(totalExpenses.add(debtRepayment));

        Budget budget = new Budget();
        budget.setTotalIncome(totalIncome);
        budget.setTotalExpenses(totalExpenses);
        budget.setDebtRepayment(debtRepayment);
        budget.setBudgetBalance(budgetBalance);
        
        
        budgetRepository.save(budget);

        return budget;
    }
    
    public int getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getUserId();
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
    
    public String getAccountNumberById(int accountId) {
        return bankAccountRepository.findAccountNumberById(accountId);
    }

}
