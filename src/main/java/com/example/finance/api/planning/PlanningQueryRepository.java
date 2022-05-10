package com.example.finance.api.planning;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlanningQueryRepository {

    Page<Planning> findByFilter(PlanningFilter filter, Pageable pageable);

    List<Planning> findToCreateBill();
}
