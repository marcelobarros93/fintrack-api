package com.example.finance.api.income;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IncomeQueryRepository {

    public Page<Income> findByFilter(IncomeFilter filter, Pageable pageable, String userId);
}
