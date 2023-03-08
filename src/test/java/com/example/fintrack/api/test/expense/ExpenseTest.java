package com.example.fintrack.api.test.expense;

import com.example.fintrack.api.common.enums.StatusType;
import com.example.fintrack.api.common.exception.BusinessException;
import com.example.fintrack.api.expense.Expense;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpenseTest {

    @Test
    void create_ShouldCreateOpenExpense_WhenCreate() {
        var userId = UUID.randomUUID().toString();
        Expense expense = Expense.builder()
                .id(1L)
                .description(description())
                .amount(BigDecimal.TEN)
                .status(StatusType.PAID)
                .dateDue(LocalDate.now())
                .build();

        expense.create(userId);

        assertThat(expense.getStatus()).isEqualTo(StatusType.OPEN);
    }

    @Test
    void update_ShouldThrowsBusinessException_WhenExpenseIsPaidAndDatePaymentIsNull() {
        Expense expense = Expense.builder()
                .id(1L)
                .status(StatusType.PAID)
                .datePayment(null)
                .build();

        assertThrows(BusinessException.class, expense::update);
    }

    @Test
    void pay_ShouldThrowsBusinessException_WhenExpenseIsPaid() {
        Expense expense = Expense.builder()
                .id(1L)
                .status(StatusType.PAID)
                .build();

        assertThrows(BusinessException.class, expense::pay);
    }

    @Test
    void cancelPayment_ShouldThrowsBusinessException_WhenCancelNotPaidExpense() {
        Expense expense = Expense.builder()
                .id(1L)
                .status(StatusType.OPEN)
                .build();

        assertThrows(BusinessException.class, expense::cancelPayment);
    }

    private String description() {
        return RandomStringUtils.randomAlphabetic(10);
    }

}
