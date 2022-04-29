package com.example.finance.api.income;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;

@RequiredArgsConstructor
public class IncomeQueryRepositoryImpl implements IncomeQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired @Lazy
    private IncomeRepository incomeRepository;

    @Override
    public Page<Income> findByFilter(IncomeFilter filter, Pageable pageable) {
        return incomeRepository.findAll(getSpecByFilter(filter), pageable);
    }

    private Specification<Income> getSpecByFilter(IncomeFilter filter) {
        return (income, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

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
                        income.get("dateDueStart"), filter.dateDueStart()));
            }

            if(filter.dateDueEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(
                        income.get("dateDueEnd"), filter.dateDueEnd()));
            }

            if(filter.dateReceiptStart() != null) {
                predicates.add(builder.greaterThanOrEqualTo(
                        income.get("dateReceiptStart"), filter.dateReceiptStart()));
            }

            if(filter.dateReceiptEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(
                        income.get("dateReceiptEnd"), filter.dateReceiptEnd()));
            }

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
