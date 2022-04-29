package com.example.finance.api.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends
        JpaRepository<Expense, Long>,
        ExpenseQueryRepository,
        JpaSpecificationExecutor<Expense> { }