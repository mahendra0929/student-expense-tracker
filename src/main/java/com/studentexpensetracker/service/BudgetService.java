package com.studentexpensetracker.service;

import com.studentexpensetracker.dto.BudgetStatusResponse;
import com.studentexpensetracker.model.Budget;
import com.studentexpensetracker.model.User;
import com.studentexpensetracker.repository.BudgetRepository;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final ExpenseService expenseService;

    public BudgetService(BudgetRepository budgetRepository, ExpenseService expenseService) {
        this.budgetRepository = budgetRepository;
        this.expenseService = expenseService;
    }

    public Budget saveBudget(Budget budget, User user) {
        return budgetRepository.findByUserIdAndMonth(user.getId(), budget.getMonth())
                .map(existingBudget -> {
                    existingBudget.setLimitAmount(budget.getLimitAmount());
                    return budgetRepository.save(existingBudget);
                })
                .orElseGet(() -> {
                    budget.setUser(user);
                    return budgetRepository.save(budget);
                });
    }

    public BudgetStatusResponse checkBudget(String month, Long userId) {
        Budget budget = budgetRepository.findByUserIdAndMonth(userId, month)
                .orElseGet(() -> {
                    Budget defaultBudget = new Budget();
                    defaultBudget.setMonth(month);
                    defaultBudget.setLimitAmount(0);
                    return defaultBudget;
                });

        double spentAmount = expenseService.getSpendingForMonth(month, userId);
        double remainingAmount = budget.getLimitAmount() - spentAmount;
        boolean exceeded = budget.getLimitAmount() > 0 && spentAmount > budget.getLimitAmount();

        String message;
        if (budget.getLimitAmount() <= 0) {
            message = "No budget set for " + month;
        } else if (exceeded) {
            message = "Warning: Budget exceeded!";
        } else {
            message = "Budget is under control";
        }

        return new BudgetStatusResponse(
                month,
                budget.getLimitAmount(),
                spentAmount,
                remainingAmount,
                exceeded,
                message
        );
    }
}
