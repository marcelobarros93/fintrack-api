package com.example.finance.api.income;

import com.example.finance.api.common.dto.MonthlyTotalDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class IncomeQueryRepositoryImpl implements IncomeQueryRepository {

    @Autowired @Lazy
    private IncomeRepository incomeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Income> findByFilter(IncomeFilter filter, Pageable pageable, String userId) {
        return incomeRepository.findAll(getSpecByFilter(filter, userId), pageable);
    }

    @Override
    public List<MonthlyTotalDTO> findTotalAmountByGivenMonthAndPreviousMonth(YearMonth month, String userId) {
        String sql = """
            select to_char(e.date_due, 'yyyy-mm') as month, sum(e.amount) as total
            from tb_income e
            where (date_trunc_month(e.date_due) = date_trunc_month(:givenMonth)
            or date_trunc_month(e.date_due) = date_trunc_month(:givenMonth) - INTERVAL '1 month')
            and e.user_id = :userId
            group by to_char(e.date_due, 'yyyy-mm')
            order by to_char(e.date_due, 'yyyy-mm') asc;
        """;

        Query query = entityManager.unwrap(Session.class).createSQLQuery(sql);
        query.setParameter("givenMonth", month.atDay(1));
        query.setParameter("userId", userId);
        query.setResultTransformer(Transformers.aliasToBean(MonthlyTotalDTO.class));
        return query.getResultList();
    }

    @Override
    public List<MonthlyTotalDTO> findTotalAmountByPeriod(YearMonth start, YearMonth end, String userId) {
        String sql = """
            select to_char(e.date_due, 'yyyy-mm') as month, sum(e.amount) as total
            from tb_income e
            where date_trunc_month(e.date_due) >= date_trunc_month(:start)
            and date_trunc_month(e.date_due) <= date_trunc_month(:end)
            and e.user_id = :userId
            group by to_char(e.date_due, 'yyyy-mm')
            order by to_char(e.date_due, 'yyyy-mm') asc;
        """;

        Query query = entityManager.unwrap(Session.class).createSQLQuery(sql);
        query.setParameter("start", start.atDay(1));
        query.setParameter("end", end.atDay(1));
        query.setParameter("userId", userId);
        query.setResultTransformer(Transformers.aliasToBean(MonthlyTotalDTO.class));
        return query.getResultList();
    }

    private Specification<Income> getSpecByFilter(IncomeFilter filter, String userId) {
        return (income, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            predicates.add(builder.equal(income.get("userId"), userId));

            if(filter.status() != null) {
                predicates.add(builder.equal(income.get("status"), filter.status()));
            }

            if(StringUtils.isNotBlank(filter.description())) {
                predicates.add(builder.like(
                                builder.lower(income.get("description")),
                                "%" + filter.description().toLowerCase() + "%"));
            }

            if(filter.dateDueStart() != null) {
                predicates.add(builder.greaterThanOrEqualTo(
                        income.get("dateDue"), filter.dateDueStart()));
            }

            if(filter.dateDueEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(
                        income.get("dateDue"), filter.dateDueEnd()));
            }

            if(filter.dateReceiptStart() != null) {
                predicates.add(builder.greaterThanOrEqualTo(
                        income.get("dateReceipt"), filter.dateReceiptStart()));
            }

            if(filter.dateReceiptEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(
                        income.get("dateReceipt"), filter.dateReceiptEnd()));
            }

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
