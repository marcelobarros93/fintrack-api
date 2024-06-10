package com.example.fintrack.api.expense;

import com.example.fintrack.api.category.Category;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseCreateRequest(
        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
        BigDecimal amount,

        @NotNull(message = "Date due is required")
        LocalDate dateDue,

        Long categoryId

) {
        public Expense toEntity() {
                return Expense.builder()
                        .description(description)
                        .amount(amount)
                        .dateDue(dateDue)
                        .category(categoryId != null ? new Category(categoryId) : null)
                        .build();
        }
}
