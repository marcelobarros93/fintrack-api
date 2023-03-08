package com.example.fintrack.api.planning;

import com.example.fintrack.api.common.enums.BillType;

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
