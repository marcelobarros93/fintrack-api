package com.example.fintrack.api.planning;

import com.example.fintrack.api.common.enums.BillType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.YearMonth;

public record PlanningRequest(

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Due day is required")
        @Min(value = 1, message = "Due day cannot less than 1")
        @Max(value = 31, message = "Due day cannot greater than 31")
        Integer dueDay,

        @NotNull(message = "Type is required")
        BillType type,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
        BigDecimal amount,

        @NotNull(message = "Date start is required")
        YearMonth startAt,

        @NotNull(message = "Date end is required")
        YearMonth endAt,

        @NotNull(message = "Active is required")
        Boolean active,

        @NotNull(message = "Show installments in bill name is required")
        Boolean showInstallmentsInBillName,

        Long categoryId
) { }
