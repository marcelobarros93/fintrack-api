package com.example.finance.api.income;

import com.example.finance.api.common.enums.StatusType;

import java.time.LocalDate;

public record IncomeFilter(
     StatusType status,
     String description,
     LocalDate dateDueStart,
     LocalDate dateDueEnd,
     LocalDate dateReceiptStart,
     LocalDate dateReceiptEnd
) { }
