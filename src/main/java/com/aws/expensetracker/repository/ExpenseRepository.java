package com.aws.expensetracker.repository;

import com.aws.expensetracker.model.Expense;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ExpenseRepository {
    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    private DynamoDbTable<Expense> table;

    @PostConstruct
    private void init() {
        table = enhancedClient.table("Expenses", TableSchema.fromBean(Expense.class));
    }

    public void save(Expense expense) {
        table.putItem(expense);
    }

    public List<Expense> findAll() {
        return table.scan().items().stream().collect(Collectors.toList());
    }

    public Expense findById(String id) {
        return table.getItem(Key.builder().partitionValue(id).build());
    }

    public void delete(String id) {
        table.deleteItem(Key.builder().partitionValue(id).build());
    }
}