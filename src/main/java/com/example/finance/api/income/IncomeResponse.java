package com.example.finance.api.income;

import com.example.finance.api.common.enums.StatusType;

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
