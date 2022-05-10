package com.example.finance.api.planning;

import com.example.finance.api.common.entity.AbstractEntity;
import com.example.finance.api.common.enums.BillType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Builder
    public Planning(Long id, String description, BigDecimal amount, Integer dueDay, BillType type,
                    LocalDate startAt, LocalDate endAt, Boolean active) {
        super(id);
        this.description = description;
        this.amount = amount;
        this.dueDay = dueDay;
        this.type = type;
        this.startAt = startAt;
        this.endAt = endAt;
        this.active = active;
    }

    
}