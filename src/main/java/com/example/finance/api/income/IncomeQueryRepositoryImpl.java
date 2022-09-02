package com.example.finance.api.income;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;

@RequiredArgsConstructor
public class IncomeQueryRepositoryImpl implements IncomeQueryRepository {

    @Autowired @Lazy
    private IncomeRepository incomeRepository;

    @Override
    public Page<Income> findByFilter(IncomeFilter filter, Pageable pageable, String userId) {
        return incomeRepository.findAll(getSpecByFilter(filter, userId), pageable);
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
