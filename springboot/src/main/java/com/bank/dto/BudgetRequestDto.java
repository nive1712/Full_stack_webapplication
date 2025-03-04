package com.bank.dto;

import java.math.BigDecimal;

public class BudgetRequestDto {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal debtRepayment;

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
}
