package com.example.finance.api.test.expense;

import com.example.finance.api.common.enums.StatusType;
import com.example.finance.api.common.exception.BusinessException;
import com.example.finance.api.expense.Expense;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpenseTest {

    @Test
    void create_ShouldCreateOpenExpense_WhenCreate() {
        Expense expense = Expense.builder()
                .id(1L)
                .description(description())
                .amount(BigDecimal.TEN)
                .status(StatusType.PAID)
                .dateDue(LocalDate.now())
                .build();

        expense.create();

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
