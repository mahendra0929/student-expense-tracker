package com.studentexpensetracker.repository;

import com.studentexpensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByUserIdOrderByDateDescIdDesc(Long userId);
    List<Expense> findAllByUserId(Long userId);
    Optional<Expense> findByIdAndUserId(Long id, Long userId);
}
