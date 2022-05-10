package com.example.finance.api.planning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanningRepository extends JpaRepository<Planning, Long>, JpaSpecificationExecutor<Planning>, PlanningQueryRepository {
}