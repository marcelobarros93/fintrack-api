package com.example.fintrack.api.category;

import com.example.fintrack.api.common.enums.BillType;
import com.example.fintrack.api.common.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findByIdAndUserIdAndType(Long id, String userId, BillType type) {
        return categoryRepository.findByIdAndUserIdAndType(id, userId, type)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
