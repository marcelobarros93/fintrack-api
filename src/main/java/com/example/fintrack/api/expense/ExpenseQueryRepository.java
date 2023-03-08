package com.example.fintrack.api.expense;

import com.example.fintrack.api.common.dto.MonthlyTotalDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;
import java.util.List;

public interface ExpenseQueryRepository {

    Page<Expense> findByFilter(ExpenseFilter filter, Pageable pageable, String userId);

    List<MonthlyTotalDTO> findTotalAmountByGivenMonthAndPreviousMonth(YearMonth month, String userId);

    List<MonthlyTotalDTO> findTotalAmountByPeriod(YearMonth start, YearMonth end, String userId);
}
