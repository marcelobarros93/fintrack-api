package com.example.fintrack.api.planning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanningRepository extends JpaRepository<Planning, Long>, JpaSpecificationExecutor<Planning>, PlanningQueryRepository {

    Optional<Planning> findByIdAndUserId(Long id, String userId);

    boolean existsByIdAndUserId(Long id, String userId);
}