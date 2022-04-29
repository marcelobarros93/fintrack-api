package com.example.finance.api.common.entity;

import com.example.finance.api.common.enums.StatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public abstract class Bill {

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

    public Bill(String description, BigDecimal amount, StatusType status, LocalDate dateDue) {
        this.description = description;
        this.amount = amount;
        this.status = status;
        this.dateDue = dateDue;
    }

    public boolean isPaid() {
        return StatusType.PAID.equals(status);
    }

    public boolean isReceived() {
        return StatusType.RECEIVED.equals(status);
    }
}