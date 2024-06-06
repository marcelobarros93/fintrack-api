package com.example.fintrack.api.common.exception;

public class CategoryNotFoundException extends EntityNotFoundException {
    public CategoryNotFoundException(Long id) {
        super(String.format("Category with id: %s is not found", id));
    }
}
