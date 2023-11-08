package com.example.fintrack.api.common.entity;

import com.example.fintrack.api.common.enums.StatusType;
import com.example.fintrack.api.planning.Planning;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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