package com.studentexpensetracker.service;

import com.studentexpensetracker.dto.ExpenseSummaryResponse;
import com.studentexpensetracker.exception.ResourceNotFoundException;
import com.studentexpensetracker.model.Expense;
import com.studentexpensetracker.model.User;
import com.studentexpensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense addExpense(Expense expense, User user) {
        expense.setUser(user);
        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses(Long userId) {
        return expenseRepository.findAllByUserIdOrderByDateDescIdDesc(userId);
    }

    public Expense getExpenseById(Long id, Long userId) {
        return expenseRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id " + id));
    }

    public Expense updateExpense(Long id, Expense updatedExpense, Long userId) {
        Expense existingExpense = getExpenseById(id, userId);
        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setCategory(updatedExpense.getCategory());
        existingExpense.setDate(updatedExpense.getDate());
        existingExpense.setDescription(updatedExpense.getDescription());
        return expenseRepository.save(existingExpense);
    }

    public void deleteExpense(Long id, Long userId) {
        Expense existingExpense = getExpenseById(id, userId);
        expenseRepository.delete(existingExpense);
    }

    public ExpenseSummaryResponse getSummary(Long userId) {
        List<Expense> expenses = expenseRepository.findAllByUserId(userId);
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.now();

        double totalSpending = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double todaySpending = expenses.stream()
                .filter(expense -> today.equals(expense.getDate()))
                .mapToDouble(Expense::getAmount)
                .sum();
        double currentMonthSpending = expenses.stream()
                .filter(expense -> YearMonth.from(expense.getDate()).equals(currentMonth))
                .mapToDouble(Expense::getAmount)
                .sum();

        Map<String, Double> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(expense -> expense.getCategory().name(),
                        LinkedHashMap::new,
                        Collectors.summingDouble(Expense::getAmount)));

        Map<String, Double> monthlyTotals = expenses.stream()
                .collect(Collectors.groupingBy(expense -> YearMonth.from(expense.getDate()).toString(),
                        LinkedHashMap::new,
                        Collectors.summingDouble(Expense::getAmount)));

        Map<String, Double> sortedMonthlyTotals = monthlyTotals.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> right,
                        LinkedHashMap::new
                ));

        return new ExpenseSummaryResponse(totalSpending, todaySpending, currentMonthSpending, categoryTotals, sortedMonthlyTotals);
    }

    public double getSpendingForMonth(String month, Long userId) {
        YearMonth targetMonth = YearMonth.parse(month);
        return expenseRepository.findAllByUserId(userId).stream()
                .filter(expense -> YearMonth.from(expense.getDate()).equals(targetMonth))
                .mapToDouble(Expense::getAmount)
                .sum();
    }
}
