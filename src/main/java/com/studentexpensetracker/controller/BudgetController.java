package com.studentexpensetracker.controller;

import com.studentexpensetracker.dto.BudgetStatusResponse;
import com.studentexpensetracker.model.Budget;
import com.studentexpensetracker.model.User;
import com.studentexpensetracker.service.AuthService;
import com.studentexpensetracker.service.BudgetService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/budgets")
@Validated
public class BudgetController {

    private final BudgetService budgetService;
    private final AuthService authService;

    public BudgetController(BudgetService budgetService, AuthService authService) {
        this.budgetService = budgetService;
        this.authService = authService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Budget saveBudget(@Valid @RequestBody Budget budget, HttpSession session) {
        User currentUser = authService.requireLoggedInUser(session);
        return budgetService.saveBudget(budget, currentUser);
    }

    @GetMapping("/check")
    public BudgetStatusResponse checkBudget(
            @RequestParam
            @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "Month must be in YYYY-MM format")
            String month,
            HttpSession session) {
        User currentUser = authService.requireLoggedInUser(session);
        return budgetService.checkBudget(month, currentUser.getId());
    }
}
