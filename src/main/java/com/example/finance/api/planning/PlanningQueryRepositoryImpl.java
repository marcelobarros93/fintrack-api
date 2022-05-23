package com.example.finance.api.planning;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PlanningQueryRepositoryImpl implements PlanningQueryRepository {

    @Autowired @Lazy
    private PlanningRepository planningRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Planning> findByFilter(PlanningFilter filter, Pageable pageable) {
        return planningRepository.findAll(getSpecByFilter(filter), pageable);
    }

    @Override
    public List<Planning> findToCreateBill() {
        String sql = """
            select * from tb_planning p
            where p.active = true
            and to_char(p.start_at, 'MM-YYYY') <= to_char(now(), 'MM-YYYY')
            and to_char(p.end_at, 'MM-YYYY') >= to_char(now(), 'MM-YYYY')
            and p.id not in (select planning_id from tb_expense e where e.planning_id is not null
                                and to_char(e.date_due , 'MM-YYYY') = to_char(now(), 'MM-YYYY'))
            and p.id not in (select planning_id from tb_income i where i.planning_id is not null
                                and to_char(i.date_due , 'MM-YYYY') = to_char(now(), 'MM-YYYY'));
        """;
        Query query = entityManager.createNativeQuery(sql, Planning.class);
        return query.getResultList();
    }

    private Specification<Planning> getSpecByFilter(PlanningFilter filter) {
        return (planning, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

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
                predicates.add(builder.greaterThanOrEqualTo(planning.get("startAt"), filter.startAtStart()));
            }

            if(filter.startAtEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(planning.get("startAt"), filter.startAtEnd()));
            }

            if(filter.endAtStart() != null) {
                predicates.add(builder.greaterThanOrEqualTo(planning.get("endAt"), filter.endAtStart()));
            }

            if(filter.endAtEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(planning.get("endAt"), filter.endAtEnd()));
            }

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
