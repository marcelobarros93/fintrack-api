package com.example.finance.api.income;

import com.example.finance.api.common.dto.MonthlyTotalDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;
import java.util.List;

public interface IncomeQueryRepository {

    Page<Income> findByFilter(IncomeFilter filter, Pageable pageable, String userId);
    List<MonthlyTotalDTO> findTotalAmountByGivenMonthAndPreviousMonth(YearMonth month, String userId);
    List<MonthlyTotalDTO> findTotalAmountByPeriod(YearMonth start, YearMonth end, String userId);
}
