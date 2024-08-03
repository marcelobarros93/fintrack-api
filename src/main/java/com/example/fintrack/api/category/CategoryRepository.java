package com.example.fintrack.api.category;

import com.example.fintrack.api.common.enums.BillType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserIdAndTypeOrderByName(String userId, BillType type);

    Optional<Category> findByIdAndUserIdAndType(Long id, String userId, BillType type);

    boolean existsByUserIdAndNameAndType(String userId, String name, BillType type);
}