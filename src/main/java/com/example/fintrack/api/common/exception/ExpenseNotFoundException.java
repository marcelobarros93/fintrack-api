package com.example.fintrack.api.common.exception;

public class ExpenseNotFoundException extends EntityNotFoundException {

    public ExpenseNotFoundException(Long id) {
        super(String.format("Expense with id: %s is not found", id));
    }
}
