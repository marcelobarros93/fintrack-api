package com.example.finance.api.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseRepository extends
        JpaRepository<Expense, Long>,
        ExpenseQueryRepository,
        JpaSpecificationExecutor<Expense> {

    Optional<Expense> findByIdAndUserId(Long id, String userId);

    boolean existsByIdAndUserId(Long id, String userId);
}