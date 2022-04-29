package com.example.finance.api.income;

import com.example.finance.api.common.enums.StatusType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record IncomeResponse(
        Long id,
        String description,
        BigDecimal amount,
        LocalDate dateDue,
        LocalDateTime dateReceipt,
        StatusType status
) { }
