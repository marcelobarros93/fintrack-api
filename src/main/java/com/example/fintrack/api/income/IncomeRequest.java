package com.example.fintrack.api.income;

import com.example.fintrack.api.common.enums.StatusType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record IncomeRequest(
        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
        BigDecimal amount,

        @NotNull(message = "Date due is required")
        LocalDate dateDue,

        OffsetDateTime dateReceipt,

        @NotNull(message = "Status is required")
        StatusType status
) { }
