package com.example.fintrack.api.common.entity;

import com.example.fintrack.api.category.Category;
import com.example.fintrack.api.common.enums.StatusType;
import com.example.fintrack.api.planning.Planning;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planning_id")
    private Planning planning;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    protected Bill(Long id, String description, BigDecimal amount,
                   StatusType status, LocalDate dateDue,
                   Planning planning, Category category) {
        super(id);
        this.description = description;
        this.amount = amount;
        this.status = status;
        this.dateDue = dateDue;
        this.planning = planning;
        this.category = category;
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