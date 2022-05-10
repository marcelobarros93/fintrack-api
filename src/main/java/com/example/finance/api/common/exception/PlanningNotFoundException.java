package com.example.finance.api.common.exception;

public class PlanningNotFoundException extends EntityNotFoundException {

    public PlanningNotFoundException(Long id) {
        super(String.format("Planning with id: %s is not found", id));
    }
}
