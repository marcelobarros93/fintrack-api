package com.example.fintrack.api.planning;

import com.example.fintrack.api.common.enums.BillType;

import java.math.BigDecimal;
import java.time.YearMonth;

public record PlanningResponse(
        Long id,
        String description,
        Integer dueDay,
        BillType type,
        Boolean active,
        BigDecimal amount,
        YearMonth startAt,
        YearMonth endAt
) { }
