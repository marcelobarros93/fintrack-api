package com.example.finance.api.planning;

import com.example.finance.api.common.enums.BillType;

import java.time.YearMonth;

public record PlanningFilter(
        String description,
        Integer dueDay,
        BillType type,
        Boolean active,
        YearMonth startAtStart,
        YearMonth startAtEnd,
        YearMonth endAtStart,
        YearMonth endAtEnd
) { }
