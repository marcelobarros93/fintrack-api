package com.example.fintrack.api.statistics;

import java.io.Serializable;
import java.math.BigDecimal;

public record Balance (
    BigDecimal totalIncomeGivenMonth,
    BigDecimal totalIncomeLastMonth,
    BigDecimal differencePercentageIncome,
    BigDecimal totalExpenseGivenMonth,
    BigDecimal totalExpenseLastMonth,
    BigDecimal differencePercentageExpense,
    BigDecimal balanceGivenMonth,
    BigDecimal balanceLastMonth,
    BigDecimal differencePercentageBalance
) implements Serializable { }
