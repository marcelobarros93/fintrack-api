package com.example.finance.api.expense;

import com.example.finance.api.common.entity.Bill;
import com.example.finance.api.common.enums.StatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_expense")
public class Expense extends Bill {

    @Column(name = "date_payment")
    private LocalDateTime datePayment;

    @Builder
    public Expense(Long id, String description, BigDecimal amount, StatusType status,
                    LocalDate dateDue, LocalDateTime datePayment) {
        super(id, description, amount, status, dateDue);
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

}