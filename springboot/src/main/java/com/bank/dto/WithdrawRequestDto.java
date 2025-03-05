package com.bank.dto;
public class WithdrawRequestDto {
    private double amount;
    private String accountNumber;
    private int pin;
    public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

}
