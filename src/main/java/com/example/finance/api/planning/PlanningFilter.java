package com.example.finance.api.planning;

import com.example.finance.api.common.enums.BillType;

import java.time.LocalDate;

public record PlanningFilter(
        String description,
        Integer dueDay,
        BillType type,
        Boolean active,
        LocalDate startAtStart,
        LocalDate startAtEnd,
        LocalDate endAtStart,
        LocalDate endAtEnd
) { }
