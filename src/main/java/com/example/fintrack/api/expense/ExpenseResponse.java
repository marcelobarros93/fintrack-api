package com.example.fintrack.api.expense;

import com.example.fintrack.api.common.enums.StatusType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record ExpenseResponse (
        Long id,
        String description,
        BigDecimal amount,
        LocalDate dateDue,
        OffsetDateTime datePayment,
        StatusType status,
        Long categoryId
) { }
