package com.example.finance.api.expense;

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
public class ExpenseQueryRepositoryImpl implements ExpenseQueryRepository {

    @Autowired @Lazy
    private ExpenseRepository expenseRepository;

    @Override
    public Page<Expense> findByFilter(ExpenseFilter filter, Pageable pageable) {
        return expenseRepository.findAll(getSpecByFilter(filter), pageable);
    }

    private Specification<Expense> getSpecByFilter(ExpenseFilter filter) {
        return (expense, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            if(filter.status() != null) {
                predicates.add(builder.equal(expense.get("status"), filter.status()));
            }

            if(StringUtils.isNotBlank(filter.description())) {
                predicates.add(builder.like(
                                builder.lower(expense.get("description")),
                                "%" + filter.description().toLowerCase() + "%"));
            }

            if(filter.dateDueStart() != null) {
                predicates.add(builder.greaterThanOrEqualTo(
                        expense.get("dateDue"), filter.dateDueStart()));
            }

            if(filter.dateDueEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(
                        expense.get("dateDue"), filter.dateDueEnd()));
            }

            if(filter.datePaymentStart() != null) {
                predicates.add(builder.greaterThanOrEqualTo(
                        expense.get("datePayment"), filter.datePaymentStart()));
            }

            if(filter.datePaymentEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(
                        expense.get("datePayment"), filter.datePaymentEnd()));
            }

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
