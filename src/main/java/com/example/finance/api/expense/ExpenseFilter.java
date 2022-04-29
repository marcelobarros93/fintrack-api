package com.example.finance.api.expense;

import com.example.finance.api.common.enums.StatusType;

import java.time.LocalDate;

public record ExpenseFilter (
     StatusType status,
     String description,
     LocalDate dateDueStart,
     LocalDate dateDueEnd,
     LocalDate datePaymentStart,
     LocalDate datePaymentEnd
) { }
