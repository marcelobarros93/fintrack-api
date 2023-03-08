package com.example.fintrack.api.income;

import com.example.fintrack.api.common.enums.StatusType;

import java.time.LocalDate;

public record IncomeFilter(
     StatusType status,
     String description,
     LocalDate dateDueStart,
     LocalDate dateDueEnd,
     LocalDate dateReceiptStart,
     LocalDate dateReceiptEnd
) { }
