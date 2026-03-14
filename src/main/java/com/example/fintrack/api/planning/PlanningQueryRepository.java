package com.example.fintrack.api.planning;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PlanningQueryRepository {

    Page<Planning> findByFilter(PlanningFilter filter, Pageable pageable, String userId);

    List<Planning> findToCreateBill(LocalDate referenceDate);
}
