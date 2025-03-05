package com.bank.dto;
import java.math.BigDecimal;

public class TransferRequestDto {
    private String senderAccountNumber;
    private String recipientAccountNumber;
    private int pin;
    public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	private BigDecimal amount;

    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

   
    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }
    public String getRecipientAccountNumber() {
        return recipientAccountNumber;
    }

    public void setRecipientAccountNumber(String recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
