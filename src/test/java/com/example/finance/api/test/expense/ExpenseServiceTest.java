package com.example.finance.api.test.expense;

import com.example.finance.api.common.enums.StatusType;
import com.example.finance.api.common.exception.BusinessException;
import com.example.finance.api.expense.Expense;
import com.example.finance.api.expense.ExpenseRepository;
import com.example.finance.api.expense.ExpenseService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ExpenseServiceTest {

    @InjectMocks
    private ExpenseService expenseService;

    @Mock
    private ExpenseRepository expenseRepository;

    @Test
    void create_ShouldCreateOpenExpense_WhenCreate() {
        Expense expense = Expense.builder()
                .id(1L)
                .description(description())
                .amount(BigDecimal.TEN)
                .status(StatusType.PAID)
                .dateDue(LocalDate.now())
                .build();

        when(expenseRepository.save(expense)).thenReturn(expense);

        Expense expenseSaved = expenseService.create(expense);

        verify(expenseRepository).save(any());
        assertThat(expenseSaved.getStatus()).isEqualTo(StatusType.OPEN);
    }

    @Test
    void update_ShouldThrowsBusinessException_WhenExpenseIsPaidAndDatePaymentIsNull() {
        Expense expense = Expense.builder()
                .id(1L)
                .status(StatusType.PAID)
                .datePayment(null)
                .build();
        assertThrows(BusinessException.class, () -> expenseService.update(1L, expense));
    }

    @Test
    void pay_ShouldThrowsBusinessException_WhenExpenseIsPaid() {
        Expense expense = Expense.builder()
                .id(1L)
                .status(StatusType.PAID)
                .build();

        when(expenseRepository.findById(any())).thenReturn(Optional.of(expense));
        assertThrows(BusinessException.class, () -> expenseService.pay(1L));
        verify(expenseRepository).findById(any());
    }

    @Test
    void cancelPayment_ShouldThrowsBusinessException_WhenCancelNotPaidExpense() {
        Expense expense = Expense.builder()
                .id(1L)
                .status(StatusType.OPEN)
                .build();

        when(expenseRepository.findById(any())).thenReturn(Optional.of(expense));
        assertThrows(BusinessException.class, () -> expenseService.cancelPayment(1L));
        verify(expenseRepository).findById(any());
    }

    private String description() {
        return RandomStringUtils.randomAlphabetic(10);
    }

}
