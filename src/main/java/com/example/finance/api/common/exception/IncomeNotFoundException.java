package com.example.finance.api.common.exception;

public class IncomeNotFoundException extends EntityNotFoundException {

    public IncomeNotFoundException(Long id) {
        super(String.format("Income with id: %s is not found", id));
    }
}
