package com.example.fintrack.api.test.income;

import com.example.fintrack.api.common.enums.StatusType;
import com.example.fintrack.api.common.exception.BusinessException;
import com.example.fintrack.api.income.Income;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IncomeTest {

    @Test
    void create_ShouldCreateOpenIncome_WhenValidParameters() {
        var userId = UUID.randomUUID().toString();
        Income income = Income.builder()
                .id(1L)
                .description(description())
                .amount(BigDecimal.TEN)
                .status(StatusType.RECEIVED)
                .dateDue(LocalDate.now())
                .build();

        income.create(userId);

        assertThat(income.getStatus()).isEqualTo(StatusType.OPEN);
    }

    @Test
    void update_ShouldThrowsBusinessException_WhenIncomeIsReceivedAndDateReceiptIsNull() {
        Income income = Income.builder()
                .id(1L)
                .status(StatusType.RECEIVED)
                .dateReceipt(null)
                .build();

        assertThrows(BusinessException.class, income::update);
    }

    @Test
    void receive_ShouldThrowsBusinessException_WhenIncomeIsReceived() {
        Income income = Income.builder()
                .id(1L)
                .status(StatusType.RECEIVED)
                .build();

        assertThrows(BusinessException.class, income::receive);
    }

    @Test
    void cancelReceipt_ShouldThrowsBusinessException_WhenCancelNotReceivedIncome() {
        Income income = Income.builder()
                .id(1L)
                .status(StatusType.OPEN)
                .build();

        assertThrows(BusinessException.class, income::cancelReceipt);
    }

    private String description() {
        return RandomStringUtils.randomAlphabetic(10);
    }

}
