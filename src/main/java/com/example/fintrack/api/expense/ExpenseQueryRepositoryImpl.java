package com.example.fintrack.api.expense;

import com.example.fintrack.api.common.dto.MonthlyTotalDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ExpenseQueryRepositoryImpl implements ExpenseQueryRepository {

    private static final String USER_ID_PARAM = "userId";

    @Autowired
    @Lazy
    private ExpenseRepository expenseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Expense> findByFilter(ExpenseFilter filter, Pageable pageable, String userId) {
        return expenseRepository.findAll(getSpecByFilter(filter, userId), pageable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MonthlyTotalDTO> findTotalAmountByGivenMonthAndPreviousMonth(YearMonth month, String userId) {
        String sql = """
            SELECT to_char(e.date_due, 'yyyy-mm') AS month, CAST(sum(e.amount) AS decimal) AS total
            FROM tb_expense e
            WHERE (date_trunc_month(e.date_due) = date_trunc_month(:givenMonth)
            OR date_trunc_month(e.date_due) = date_trunc_month(:givenMonth) - INTERVAL '1 month')
            AND e.user_id = :userId
            GROUP BY to_char(e.date_due, 'yyyy-mm')
            ORDER BY to_char(e.date_due, 'yyyy-mm') ASC
        """;

        var query = entityManager.createNativeQuery(sql, "MonthlyTotalDTOMapping");
        query.setParameter("givenMonth", month.atDay(1));
        query.setParameter(USER_ID_PARAM, userId);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MonthlyTotalDTO> findTotalAmountByPeriod(YearMonth start, YearMonth end, String userId) {
        String sql = """
            SELECT to_char(e.date_due, 'yyyy-mm') AS month, CAST(sum(e.amount) AS decimal) AS total
            FROM tb_expense e
            WHERE date_trunc_month(e.date_due) >= date_trunc_month(:start)
            AND date_trunc_month(e.date_due) <= date_trunc_month(:end)
            AND e.user_id = :userId
            GROUP BY to_char(e.date_due, 'yyyy-mm')
            ORDER BY to_char(e.date_due, 'yyyy-mm') ASC
        """;

        var query = entityManager.createNativeQuery(sql, "MonthlyTotalDTOMapping");
        query.setParameter("start", start.atDay(1));
        query.setParameter("end", end.atDay(1));
        query.setParameter(USER_ID_PARAM, userId);
        return query.getResultList();
    }

    private Specification<Expense> getSpecByFilter(ExpenseFilter filter, String userId) {
        return (expense, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            predicates.add(builder.equal(expense.get(USER_ID_PARAM), userId));

            if (filter.status() != null) {
                predicates.add(builder.equal(expense.get("status"), filter.status()));
            }

            if (StringUtils.isNotBlank(filter.description())) {
                predicates.add(builder.like(
                        builder.lower(expense.get("description")),
                        "%" + filter.description().toLowerCase() + "%"));
            }

            if (filter.dateDueStart() != null) {
                predicates.add(builder.greaterThanOrEqualTo(
                        expense.get("dateDue"), filter.dateDueStart()));
            }

            if (filter.dateDueEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(
                        expense.get("dateDue"), filter.dateDueEnd()));
            }

            if (filter.datePaymentStart() != null) {
                predicates.add(builder.greaterThanOrEqualTo(
                        expense.get("datePayment"), filter.datePaymentStart()));
            }

            if (filter.datePaymentEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(
                        expense.get("datePayment"), filter.datePaymentEnd()));
            }

            query.orderBy(builder.asc(expense.get("description")));

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
