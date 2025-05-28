package com.aws.expensetracker.service;

import com.aws.expensetracker.model.Expense;
import com.aws.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repository;

    // Save or update an expense
    public void save(Expense expense) {
        repository.save(expense);
    }

    // Fetch all expenses
    public List<Expense> findAll() {
        return repository.findAll();
    }

    // Get a specific expense by ID
    public Expense findById(String id) {
        return repository.findById(id);
    }

    // Delete an expense by ID
    public void delete(String id) {
        repository.delete(id);
    }
}