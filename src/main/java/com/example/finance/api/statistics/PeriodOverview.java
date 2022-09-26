package com.example.finance.api.statistics;

import com.example.finance.api.common.dto.MonthlyTotalDTO;

import java.util.List;

public record PeriodOverview(
        List<MonthlyTotalDTO> incomes,
        List<MonthlyTotalDTO> expenses
) {
}
