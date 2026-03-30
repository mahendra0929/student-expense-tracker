package com.studentexpensetracker.dto;

public class BudgetStatusResponse {

    private String month;
    private double limitAmount;
    private double spentAmount;
    private double remainingAmount;
    private boolean exceeded;
    private String message;

    public BudgetStatusResponse(String month,
                                double limitAmount,
                                double spentAmount,
                                double remainingAmount,
                                boolean exceeded,
                                String message) {
        this.month = month;
        this.limitAmount = limitAmount;
        this.spentAmount = spentAmount;
        this.remainingAmount = remainingAmount;
        this.exceeded = exceeded;
        this.message = message;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(double limitAmount) {
        this.limitAmount = limitAmount;
    }

    public double getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(double spentAmount) {
        this.spentAmount = spentAmount;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public boolean isExceeded() {
        return exceeded;
    }

    public void setExceeded(boolean exceeded) {
        this.exceeded = exceeded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
