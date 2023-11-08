package com.example.fintrack.api.expense;

import com.example.fintrack.api.common.entity.Bill;
import com.example.fintrack.api.common.enums.StatusType;
import com.example.fintrack.api.common.exception.BusinessException;
import com.example.fintrack.api.planning.Planning;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_expense")
public class Expense extends Bill {

    @Column(name = "date_payment")
    private OffsetDateTime datePayment;

    @Builder
    public Expense(Long id, String description, BigDecimal amount, StatusType status,
                   LocalDate dateDue, OffsetDateTime datePayment, Planning planning) {
        super(id, description, amount, status, dateDue, planning);
        this.datePayment = datePayment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Expense expense = (Expense) o;
        return getId() != null && Objects.equals(getId(), expense.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void update() {
        if(isPaid() && getDatePayment() == null) {
            throw new BusinessException("Date payment is required when expense is paid.");
        }

        if(!isPaid()) {
            setDatePayment(null);
        }
    }

    public void pay() {
        if(isPaid()) {
            throw new BusinessException("This expense has already been paid");
        }

        setStatus(StatusType.PAID);
        setDatePayment(OffsetDateTime.now());
    }

    public void cancelPayment() {
        if(!isPaid()) {
            throw new BusinessException("This expense is not paid");
        }

        setStatus(StatusType.OPEN);
        setDatePayment(null);
    }
}