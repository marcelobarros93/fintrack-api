package com.example.finance.api.common.entity;

import com.example.finance.api.common.enums.StatusType;
import com.example.finance.api.planning.Planning;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public abstract class Bill extends AbstractEntity {

    @NotBlank
    private String description;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusType status;

    @NotNull
    @Column(name = "date_due")
    private LocalDate dateDue;

    @ManyToOne
    @JoinColumn(name = "planning_id")
    private Planning planning;

    @Column(name = "user_id", nullable = false)
    private String userId;

    protected Bill(Long id, String description, BigDecimal amount, StatusType status, LocalDate dateDue, Planning planning) {
        super(id);
        this.description = description;
        this.amount = amount;
        this.status = status;
        this.dateDue = dateDue;
        this.planning = planning;
    }

    public boolean isPaid() {
        return StatusType.PAID.equals(status);
    }

    public boolean isReceived() {
        return StatusType.RECEIVED.equals(status);
    }

    public void create(String userId) {
        setStatus(StatusType.OPEN);
        setUserId(userId);
    }
}