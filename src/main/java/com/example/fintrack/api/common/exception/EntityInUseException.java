package com.example.fintrack.api.common.exception;

public class EntityInUseException extends RuntimeException {

    public EntityInUseException(String message) {
        super(message);
    }
}
