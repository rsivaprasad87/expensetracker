package com.aws.expensetracker.controller;

import com.aws.expensetracker.model.Expense;
import com.aws.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseService service;

    @GetMapping
    public List<Expense> getAllExpenses() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<String> addExpense(@RequestBody Expense expense) {
        service.save(expense);
        return ResponseEntity.ok("Expense saved.");
    }

    @GetMapping("/{id}")
    public Expense getExpense(@PathVariable String id) {
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("Expense deleted.");
    }
}

