package com.studentexpensetracker.dto;

import java.util.Map;

public class ExpenseSummaryResponse {

    private double totalSpending;
    private double todaySpending;
    private double currentMonthSpending;
    private Map<String, Double> categoryTotals;
    private Map<String, Double> monthlyTotals;

    public ExpenseSummaryResponse(double totalSpending,
                                  double todaySpending,
                                  double currentMonthSpending,
                                  Map<String, Double> categoryTotals,
                                  Map<String, Double> monthlyTotals) {
        this.totalSpending = totalSpending;
        this.todaySpending = todaySpending;
        this.currentMonthSpending = currentMonthSpending;
        this.categoryTotals = categoryTotals;
        this.monthlyTotals = monthlyTotals;
    }

    public double getTotalSpending() {
        return totalSpending;
    }

    public void setTotalSpending(double totalSpending) {
        this.totalSpending = totalSpending;
    }

    public double getTodaySpending() {
        return todaySpending;
    }

    public void setTodaySpending(double todaySpending) {
        this.todaySpending = todaySpending;
    }

    public double getCurrentMonthSpending() {
        return currentMonthSpending;
    }

    public void setCurrentMonthSpending(double currentMonthSpending) {
        this.currentMonthSpending = currentMonthSpending;
    }

    public Map<String, Double> getCategoryTotals() {
        return categoryTotals;
    }

    public void setCategoryTotals(Map<String, Double> categoryTotals) {
        this.categoryTotals = categoryTotals;
    }

    public Map<String, Double> getMonthlyTotals() {
        return monthlyTotals;
    }

    public void setMonthlyTotals(Map<String, Double> monthlyTotals) {
        this.monthlyTotals = monthlyTotals;
    }
}
