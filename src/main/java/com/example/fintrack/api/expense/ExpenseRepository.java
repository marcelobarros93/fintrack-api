package com.example.fintrack.api.expense;

import com.example.fintrack.api.common.repository.CommonQueryByUserExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends
        JpaRepository<Expense, Long>,
        ExpenseQueryRepository,
        JpaSpecificationExecutor<Expense>,
        CommonQueryByUserExecutor<Expense> {
}