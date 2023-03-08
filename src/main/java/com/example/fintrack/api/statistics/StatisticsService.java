package com.example.fintrack.api.statistics;

import com.example.fintrack.api.common.dto.MonthlyTotalDTO;
import com.example.fintrack.api.expense.ExpenseRepository;
import com.example.fintrack.api.income.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.YearMonth;

@RequiredArgsConstructor
@Service
public class StatisticsService {

    private static final BigDecimal HUNDRED = new BigDecimal(100);
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public Balance getBalance(YearMonth month, String userId) {
        var expenses =
                expenseRepository.findTotalAmountByGivenMonthAndPreviousMonth(month, userId);
        BigDecimal totalExpenseGivenMonth = BigDecimal.ZERO;
        BigDecimal totalExpenseLastMonth = BigDecimal.ZERO;
        BigDecimal totalIncomeGivenMonth = BigDecimal.ZERO;
        BigDecimal totalIncomeLastMonth = BigDecimal.ZERO;

        for (MonthlyTotalDTO e : expenses) {
            if (e.getMonth().equals(month.toString())) {
                totalExpenseGivenMonth = e.getTotal();
            } else {
                totalExpenseLastMonth = e.getTotal();
            }
        }

        BigDecimal differencePercentageExpense =
                percentageDifference(totalExpenseLastMonth, totalExpenseGivenMonth);

        var incomes =
                incomeRepository.findTotalAmountByGivenMonthAndPreviousMonth(month, userId);

        for (MonthlyTotalDTO e : incomes) {
            if (e.getMonth().equals(month.toString())) {
                totalIncomeGivenMonth = e.getTotal();
            } else {
                totalIncomeLastMonth = e.getTotal();
            }
        }

        BigDecimal differencePercentageIncome =
                percentageDifference(totalIncomeLastMonth, totalIncomeGivenMonth);

        BigDecimal balanceGivenMonth = totalIncomeGivenMonth.subtract(totalExpenseGivenMonth);
        BigDecimal balanceLastMonth = totalIncomeLastMonth.subtract(totalExpenseLastMonth);
        BigDecimal differencePercentageBalance = percentageDifference(balanceLastMonth, balanceGivenMonth);

        return new Balance(totalIncomeGivenMonth, totalIncomeLastMonth, differencePercentageIncome,
                totalExpenseGivenMonth, totalExpenseLastMonth, differencePercentageExpense,
                balanceGivenMonth, balanceLastMonth, differencePercentageBalance);
    }

    public PeriodOverview getPeriodOverview(YearMonth start, YearMonth end, String userId) {
        var expenses = expenseRepository.findTotalAmountByPeriod(start, end, userId);
        var incomes = incomeRepository.findTotalAmountByPeriod(start, end, userId);

        return new PeriodOverview(incomes, expenses);
    }

    private BigDecimal percentageDifference(BigDecimal v1, BigDecimal v2) {
        if (v1 == null || v1.equals(BigDecimal.ZERO) || v2 == null || v2.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }

        return v2.subtract(v1)
                .divide(v1, MathContext.DECIMAL64)
                .multiply(HUNDRED)
                .setScale(3, RoundingMode.HALF_UP);
    }
}
