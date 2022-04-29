package com.example.finance.api.income;

import com.example.finance.api.common.entity.Bill;
import com.example.finance.api.common.enums.StatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date_receipt")
    private LocalDateTime dateReceipt;

    @Builder
    public Income(Long id, LocalDateTime dateReceipt, String description,
                  BigDecimal amount, StatusType status, LocalDate dateDue) {
        super(description, amount, status, dateDue);
        this.id = id;
        this.dateReceipt = dateReceipt;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Income expense = (Income) o;
        return id != null && Objects.equals(id, expense.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}