package com.example.finance.api.income;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncomeRepository extends
        JpaRepository<Income, Long>,
        IncomeQueryRepository,
        JpaSpecificationExecutor<Income> {

    Optional<Income> findByIdAndUserId(Long id, String userId);

    boolean existsByIdAndUserId(Long id, String userId);
}