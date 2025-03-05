package com.bank.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "total_income", precision = 15, scale = 2)
    private BigDecimal totalIncome;

    @Column(name = "total_expenses", precision = 15, scale = 2)
    private BigDecimal totalExpenses;

    @Column(name = "debt_repayment", precision = 15, scale = 2)
    private BigDecimal debtRepayment;

    @Column(name = "budget_balance", precision = 15, scale = 2)
    private BigDecimal budgetBalance;

    public Budget() {
    }

    public Budget(BigDecimal totalIncome, BigDecimal totalExpenses, BigDecimal debtRepayment, BigDecimal budgetBalance) {
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.debtRepayment = debtRepayment;
        this.budgetBalance = budgetBalance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public BigDecimal getDebtRepayment() {
        return debtRepayment;
    }

    public void setDebtRepayment(BigDecimal debtRepayment) {
        this.debtRepayment = debtRepayment;
    }

    public BigDecimal getBudgetBalance() {
        return budgetBalance;
    }

    public void setBudgetBalance(BigDecimal budgetBalance) {
        this.budgetBalance = budgetBalance;
    }
}
