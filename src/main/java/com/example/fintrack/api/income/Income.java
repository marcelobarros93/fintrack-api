package com.example.fintrack.api.income;

import com.example.fintrack.api.common.entity.Bill;
import com.example.fintrack.api.common.enums.StatusType;
import com.example.fintrack.api.common.exception.BusinessException;
import com.example.fintrack.api.planning.Planning;
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


@Getter
@Setter
@Entity
@Table(name = "tb_income")
@NoArgsConstructor
public class Income extends Bill {

    @Column(name = "date_receipt")
    private OffsetDateTime dateReceipt;

    @Builder
    public Income(Long id, OffsetDateTime dateReceipt, String description, BigDecimal amount,
                  StatusType status, LocalDate dateDue, Planning planning) {
        super(id, description, amount, status, dateDue, planning);
        this.dateReceipt = dateReceipt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Income expense = (Income) o;
        return getId() != null && Objects.equals(getId(), expense.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void update() {
        if(isReceived() && getDateReceipt() == null) {
            throw new BusinessException("Date received is required when income is received.");
        }

        if(!isReceived()) {
            setDateReceipt(null);
        }
    }

    public void receive() {
        if(isReceived()) {
            throw new BusinessException("This income has already been received");
        }

        setStatus(StatusType.RECEIVED);
        setDateReceipt(OffsetDateTime.now());
    }

    public void cancelReceipt() {
        if(!isReceived()) {
            throw new BusinessException("This income is not received");
        }

        setStatus(StatusType.OPEN);
        setDateReceipt(null);
    }
}