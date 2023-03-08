package com.example.fintrack.api.income;

import com.example.fintrack.api.common.enums.StatusType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record IncomeResponse(
        Long id,
        String description,
        BigDecimal amount,
        LocalDate dateDue,
        OffsetDateTime dateReceipt,
        StatusType status
) { }
