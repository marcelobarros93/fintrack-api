package com.example.fintrack.api.common.repository;

import java.util.Optional;

public interface CommonQueryByUserExecutor<T> {

    Optional<T> findByIdAndUserId(Long id, String userId);

    boolean existsByIdAndUserId(Long id, String userId);
}
