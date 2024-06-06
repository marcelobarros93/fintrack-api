package com.example.fintrack.api.category;

import com.example.fintrack.api.common.enums.BillType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserIdAndType(String userId, BillType type);
}