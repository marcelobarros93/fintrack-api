package com.example.finance.api.test.planning;

import com.example.finance.api.common.enums.BillType;
import com.example.finance.api.common.exception.BusinessException;
import com.example.finance.api.planning.Planning;
import com.example.finance.api.planning.PlanningRepository;
import com.example.finance.api.planning.PlanningService;
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
class PlanningServiceTest {
    
    @InjectMocks
    private PlanningService planningService;
    
    @Mock
    private PlanningRepository planningRepository;
    
    @Test
    void create_ShouldCreatePlanning_WhenValidParameters() {
        var planning = Planning.builder()
                .type(BillType.EXPENSE)
                .startAt(LocalDate.now())
                .endAt(LocalDate.now().plusYears(1))
                .amount(BigDecimal.TEN)
                .dueDay(4)
                .build();

        when(planningRepository.save(planning)).thenReturn(planning);
        Planning planningSaved = planningService.create(planning);
        verify(planningRepository).save(any());
        assertThat(planningSaved.getActive()).isTrue();
    }

    @Test
    void create_ShouldThrowsBusinessException_WhenEndAtIsBeforeToday() {
        var planning = Planning.builder()
                .type(BillType.EXPENSE)
                .startAt(LocalDate.now().minusDays(2))
                .endAt(LocalDate.now().minusDays(1))
                .amount(BigDecimal.TEN)
                .dueDay(4)
                .build();

        assertThrows(BusinessException.class, () -> planningService.create(planning));
    }

    @Test
    void create_ShouldThrowsBusinessException_WhenEndAtIsLessThanStartAt() {
        var planning = Planning.builder()
                .type(BillType.EXPENSE)
                .startAt(LocalDate.now().plusYears(1))
                .endAt(LocalDate.now().plusYears(1).minusDays(10))
                .amount(BigDecimal.TEN)
                .dueDay(4)
                .build();

        assertThrows(BusinessException.class, () -> planningService.create(planning));
    }

    @Test
    void update_ShouldThrowsBusinessException_WhenEndAtIsLessThanStartAt() {
        var planning = Planning.builder()
                .type(BillType.EXPENSE)
                .startAt(LocalDate.now().plusYears(1))
                .endAt(LocalDate.now().plusYears(1).minusDays(10))
                .amount(BigDecimal.TEN)
                .dueDay(4)
                .build();

        assertThrows(BusinessException.class, () -> planningService.update(1L, planning));
    }

    @Test
    void activateById_ShouldThrowsBusinessException_WhenPlanningIsActive() {
        var planning = Planning.builder()
                .active(Boolean.TRUE)
                .build();

        when(planningRepository.findById(any())).thenReturn(Optional.of(planning));
        assertThrows(BusinessException.class, () -> planningService.activateById(1L));
        verify(planningRepository).findById(any());
    }

    @Test
    void inactivateById_ShouldThrowsBusinessException_WhenPlanningIsInactive() {
        var planning = Planning.builder()
                .active(Boolean.FALSE)
                .build();

        when(planningRepository.findById(any())).thenReturn(Optional.of(planning));
        assertThrows(BusinessException.class, () -> planningService.inactivateById(1L));
        verify(planningRepository).findById(any());
    }
}