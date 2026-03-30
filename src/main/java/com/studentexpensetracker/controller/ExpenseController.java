package com.studentexpensetracker.controller;

import com.studentexpensetracker.dto.ExpenseSummaryResponse;
import com.studentexpensetracker.model.Expense;
import com.studentexpensetracker.model.User;
import com.studentexpensetracker.service.AuthService;
import com.studentexpensetracker.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final AuthService authService;

    public ExpenseController(ExpenseService expenseService, AuthService authService) {
        this.expenseService = expenseService;
        this.authService = authService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Expense addExpense(@Valid @RequestBody Expense expense, HttpSession session) {
        User currentUser = authService.requireLoggedInUser(session);
        return expenseService.addExpense(expense, currentUser);
    }

    @GetMapping
    public List<Expense> getAllExpenses(HttpSession session) {
        User currentUser = authService.requireLoggedInUser(session);
        return expenseService.getAllExpenses(currentUser.getId());
    }

    @GetMapping("/{id}")
    public Expense getExpense(@PathVariable Long id, HttpSession session) {
        User currentUser = authService.requireLoggedInUser(session);
        return expenseService.getExpenseById(id, currentUser.getId());
    }

    @PutMapping("/{id}")
    public Expense updateExpense(@PathVariable Long id, @Valid @RequestBody Expense expense, HttpSession session) {
        User currentUser = authService.requireLoggedInUser(session);
        return expenseService.updateExpense(id, expense, currentUser.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable Long id, HttpSession session) {
        User currentUser = authService.requireLoggedInUser(session);
        expenseService.deleteExpense(id, currentUser.getId());
    }

    @GetMapping("/summary")
    public ExpenseSummaryResponse getSummary(HttpSession session) {
        User currentUser = authService.requireLoggedInUser(session);
        return expenseService.getSummary(currentUser.getId());
    }
}
