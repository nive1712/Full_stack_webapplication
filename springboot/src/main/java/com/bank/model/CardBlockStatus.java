package com.bank.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity

@Table(name = "card_block_status")

public class CardBlockStatus {

	@Id

	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;

	@OneToOne

	@JoinColumn(name = "account_id", referencedColumnName = "accountId") 
    
	private BankAccount bankAccount;
	@Column(name = "is_card_blocked")

	private boolean isCardBlocked;

	@Column(name = "block_reason")

	private String blockReason;

	@Column(name = "block_date")

	private LocalDate blockDate;

	@Column(name = "unblock_date")

	private LocalDate unblockDate;

	private String status;

	public CardBlockStatus() {

		this.status = "Active";

	}

	public CardBlockStatus(BankAccount bankAccount, boolean isCardBlocked, String blockReason, LocalDate blockDate) {

		this.bankAccount = bankAccount;

		this.isCardBlocked = isCardBlocked;

		this.blockReason = blockReason;

		this.blockDate = blockDate;

		this.status = "Active";

	}

	public int getId() {

		return id;

	}

	public void setId(int id) {

		this.id = id;

	}

	public BankAccount getBankAccount() {

		return bankAccount;

	}

	public void setBankAccount(BankAccount bankAccount) {

		this.bankAccount = bankAccount;

	}

	public boolean isCardBlocked() {

		return isCardBlocked;

	}

	public void setCardBlocked(boolean cardBlocked) {

		isCardBlocked = cardBlocked;

	}

	public String getBlockReason() {

		return blockReason;

	}

	public void setBlockReason(String blockReason) {

		this.blockReason = blockReason;

	}

	public LocalDate getBlockDate() {

		return blockDate;

	}

	public void setBlockDate(LocalDate blockDate) {

		this.blockDate = blockDate;

	}

	public LocalDate getUnblockDate() {

		return unblockDate;

	}

	public void setUnblockDate(LocalDate unblockDate) {

		this.unblockDate = unblockDate;

	}

	public String getStatus() {

		return status;

	}

	public void setStatus(String status) {

		this.status = status;

	}

}
