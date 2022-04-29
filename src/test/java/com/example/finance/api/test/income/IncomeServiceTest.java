package com.example.finance.api.test.income;

import com.example.finance.api.common.enums.StatusType;
import com.example.finance.api.common.exception.BusinessException;
import com.example.finance.api.income.Income;
import com.example.finance.api.income.IncomeRepository;
import com.example.finance.api.income.IncomeService;
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
class IncomeServiceTest {

    @InjectMocks
    private IncomeService incomeService;

    @Mock
    private IncomeRepository incomeRepository;

    @Test
    void create_ShouldCreateOpenIncome_WhenCreate() {
        Income income = Income.builder()
                .id(1L)
                .description(description())
                .amount(BigDecimal.TEN)
                .status(StatusType.RECEIVED)
                .dateDue(LocalDate.now())
                .build();

        when(incomeRepository.save(income)).thenReturn(income);

        Income incomeSaved = incomeService.create(income);

        verify(incomeRepository).save(any());
        assertThat(incomeSaved.getStatus()).isEqualTo(StatusType.OPEN);
    }

    @Test
    void update_ShouldThrowsBusinessException_WhenIncomeIsReceivedAndDateReceiptIsNull() {
        Income income = Income.builder()
                .id(1L)
                .status(StatusType.RECEIVED)
                .dateReceipt(null)
                .build();
        assertThrows(BusinessException.class, () -> incomeService.update(1L, income));
    }

    @Test
    void receive_ShouldThrowsBusinessException_WhenIncomeIsReceived() {
        Income income = Income.builder()
                .id(1L)
                .status(StatusType.RECEIVED)
                .build();

        when(incomeRepository.findById(any())).thenReturn(Optional.of(income));
        assertThrows(BusinessException.class, () -> incomeService.receive(1L));
        verify(incomeRepository).findById(any());
    }

    @Test
    void cancelReceipt_ShouldThrowsBusinessException_WhenCancelNotReceivedIncome() {
        Income income = Income.builder()
                .id(1L)
                .status(StatusType.OPEN)
                .build();

        when(incomeRepository.findById(any())).thenReturn(Optional.of(income));
        assertThrows(BusinessException.class, () -> incomeService.cancelReceipt(1L));
        verify(incomeRepository).findById(any());
    }

    private String description() {
        return RandomStringUtils.randomAlphabetic(10);
    }

}
