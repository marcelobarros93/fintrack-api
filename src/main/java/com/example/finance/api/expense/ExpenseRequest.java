package com.example.finance.api.expense;

import com.example.finance.api.common.enums.StatusType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record ExpenseRequest(
        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
        BigDecimal amount,

        @NotNull(message = "Date due is required")
        LocalDate dateDue,

        OffsetDateTime datePayment,

        @NotNull(message = "Status is required")
        StatusType status
) { }
