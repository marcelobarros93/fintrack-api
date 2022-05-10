package com.example.finance.api.planning;

import com.example.finance.api.common.enums.BillType;

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
