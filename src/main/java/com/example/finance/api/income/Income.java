package com.example.finance.api.income;

import com.example.finance.api.common.entity.Bill;
import com.example.finance.api.common.enums.StatusType;
import com.example.finance.api.planning.Planning;
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


@Getter
@Setter
@Entity
@Table(name = "tb_income")
@NoArgsConstructor
public class Income extends Bill {

    @Column(name = "date_receipt")
    private LocalDateTime dateReceipt;

    @Builder
    public Income(Long id, LocalDateTime dateReceipt, String description, BigDecimal amount,
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
}