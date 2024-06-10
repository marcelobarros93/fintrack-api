package com.example.fintrack.api.category;

import com.example.fintrack.api.common.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findByIdAndUserId(Long id, String userId) {
        return categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
