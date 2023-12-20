package com.example.fintrack.api.planning;

import com.example.fintrack.api.common.entity.AbstractEntity;
import com.example.fintrack.api.common.enums.BillType;
import com.example.fintrack.api.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_planning")
public class Planning extends AbstractEntity {

    @NotBlank
    private String description;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    @Min(1)
    @Max(31)
    @Column(name = "due_day")
    private Integer dueDay;

    @Enumerated(EnumType.STRING)
    private BillType type;

    @NotNull
    @Column(name = "start_at")
    private LocalDate startAt;

    @NotNull
    @Column(name = "end_at")
    private LocalDate endAt;

    @NotNull
    private Boolean active;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "show_installments_bill_name")
    private Boolean showInstallmentsInBillName;

    @Builder
    public Planning(Long id, String description, BigDecimal amount, Integer dueDay, BillType type,
                    LocalDate startAt, LocalDate endAt, Boolean active, Boolean showInstallmentsInBillName) {
        super(id);
        this.description = description;
        this.amount = amount;
        this.dueDay = dueDay;
        this.type = type;
        this.startAt = startAt;
        this.endAt = endAt;
        this.active = active;
        this.showInstallmentsInBillName = showInstallmentsInBillName;
    }

    public void create(String userId) {
        validatePlanningDates();
        setActive(Boolean.TRUE);
        setUserId(userId);
    }

    private void validatePlanningDates() {
        if(getStartAt().isAfter(getEndAt())) {
            throw new BusinessException("The date start cannot be greater than the date end");
        }
    }

    public void update() {
        validatePlanningDates();
    }

    public void activate() {
        if(Boolean.TRUE.equals(getActive())) {
            throw new BusinessException("This planning is not inactive");
        }

        setActive(Boolean.TRUE);
    }

    public void inactivate() {
        if(Boolean.FALSE.equals(getActive())) {
            throw new BusinessException("This planning is not active");
        }

        setActive(Boolean.FALSE);
    }

    public String getBillName() {
        if(Boolean.TRUE.equals(showInstallmentsInBillName)) {
            var totalInstallments = ChronoUnit.MONTHS.between(startAt, endAt) + 1;
            var currentInstallment = ChronoUnit.MONTHS.between(startAt, LocalDate.now()) + 1;
            var counter = currentInstallment + "/" + totalInstallments;
            return description + " " + counter;
        } else {
            return description;
        }
    }
}