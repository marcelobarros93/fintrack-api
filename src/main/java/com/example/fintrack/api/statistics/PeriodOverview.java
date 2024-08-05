package com.example.fintrack.api.statistics;

import com.example.fintrack.api.common.dto.MonthlyTotalDTO;

import java.io.Serializable;
import java.util.List;

public record PeriodOverview(
        List<MonthlyTotalDTO> incomes,
        List<MonthlyTotalDTO> expenses
) implements Serializable { }
