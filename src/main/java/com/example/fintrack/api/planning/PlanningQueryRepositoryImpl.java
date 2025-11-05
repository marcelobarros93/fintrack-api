package com.example.fintrack.api.planning;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlanningQueryRepositoryImpl implements PlanningQueryRepository {

    public static final String DATE_TRUNC_MONTH = "date_trunc_month";

    @Autowired @Lazy
    private PlanningRepository planningRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Planning> findByFilter(PlanningFilter filter, Pageable pageable, String userId) {
        return planningRepository.findAll(getSpecByFilter(filter, userId), pageable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Planning> findToCreateBill() {
        String sql = """
            select * from tb_planning p
            where p.active = true
            and date_trunc('month', p.start_at) <= date_trunc('month', now())
            and date_trunc('month', p.end_at) >= date_trunc('month', now())
            and p.id not in (select planning_id from tb_expense e where e.planning_id is not null
                                and date_trunc('month', e.date_due) = date_trunc('month', now()))
            and p.id not in (select planning_id from tb_income i where i.planning_id is not null
                                and date_trunc('month', i.date_due) = date_trunc('month', now()));
        """;
        Query query = entityManager.createNativeQuery(sql, Planning.class);
        return query.getResultList();
    }

    private Specification<Planning> getSpecByFilter(PlanningFilter filter, String userId) {
        return (planning, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            predicates.add(builder.equal(planning.get("userId"), userId));

            if(filter.active() != null) {
                predicates.add(builder.equal(planning.get("active"), filter.active()));
            }

            if(StringUtils.isNotBlank(filter.description())) {
                predicates.add(builder.like(
                        builder.lower(planning.get("description")),
                        "%" + filter.description().toLowerCase() + "%"));
            }

            if(filter.dueDay() != null) {
                predicates.add(builder.equal(planning.get("dueDay"), filter.dueDay()));
            }

            if(filter.type() != null) {
                predicates.add(builder.equal(planning.get("type"), filter.type()));
            }

            if(filter.startAtStart() != null) {
                Expression<LocalDate> function = builder.function(DATE_TRUNC_MONTH, LocalDate.class, planning.get("startAt"));
                predicates.add(builder.greaterThanOrEqualTo(function, filter.startAtStart().atDay(1)));
            }

            if(filter.startAtEnd() != null) {
                Expression<LocalDate> function = builder.function(DATE_TRUNC_MONTH, LocalDate.class, planning.get("startAt"));
                predicates.add(builder.lessThanOrEqualTo(function, filter.startAtEnd().atDay(1)));
            }

            if(filter.endAtStart() != null) {
                Expression<LocalDate> function = builder.function(DATE_TRUNC_MONTH, LocalDate.class, planning.get("endAt"));
                predicates.add(builder.greaterThanOrEqualTo(function, filter.endAtStart().atDay(1)));
            }

            if(filter.endAtEnd() != null) {
                Expression<LocalDate> function = builder.function(DATE_TRUNC_MONTH, LocalDate.class, planning.get("endAt"));
                predicates.add(builder.lessThanOrEqualTo(function, filter.endAtEnd().atDay(1)));
            }

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
