package com.example.finance.api.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseQueryRepository {

    public Page<Expense> findByFilter(ExpenseFilter filter, Pageable pageable, String userId);
}
