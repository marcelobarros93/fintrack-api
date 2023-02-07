package com.example.finance.api.test.planning;

import com.example.finance.api.common.enums.BillType;
import com.example.finance.api.common.exception.BusinessException;
import com.example.finance.api.planning.Planning;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlanningTest {
    
    @Test
    void create_ShouldCreatePlanning_WhenValidParameters() {
        var userId = UUID.randomUUID().toString();

        var planning = Planning.builder()
                .type(BillType.EXPENSE)
                .startAt(LocalDate.now())
                .endAt(LocalDate.now().plusYears(1))
                .amount(BigDecimal.TEN)
                .dueDay(4)
                .build();

        planning.create(userId);

        assertThat(planning.getStartAt()).isBefore(planning.getEndAt());
        assertThat(planning.getEndAt()).isAfter(LocalDate.now());
        assertThat(planning.getActive()).isTrue();
    }

    @Test
    void create_ShouldThrowsBusinessException_WhenEndAtIsLessThanStartAt() {
        var userId = UUID.randomUUID().toString();

        var planning = Planning.builder()
                .type(BillType.EXPENSE)
                .startAt(LocalDate.now().plusYears(1))
                .endAt(LocalDate.now().plusYears(1).minusDays(10))
                .amount(BigDecimal.TEN)
                .dueDay(4)
                .build();

        assertThrows(BusinessException.class, () ->  planning.create(userId));
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

        assertThrows(BusinessException.class, planning::update);
    }

    @Test
    void activate_ShouldThrowsBusinessException_WhenPlanningIsActive() {
        var planning = Planning.builder()
                .active(Boolean.TRUE)
                .build();

        assertThrows(BusinessException.class, planning::activate);
    }

    @Test
    void inactivate_ShouldThrowsBusinessException_WhenPlanningIsInactive() {
        var planning = Planning.builder()
                .active(Boolean.FALSE)
                .build();

        assertThrows(BusinessException.class, planning::inactivate);
    }
}