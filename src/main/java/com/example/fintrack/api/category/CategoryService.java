package com.example.fintrack.api.category;

import com.example.fintrack.api.common.enums.BillType;
import com.example.fintrack.api.common.exception.CategoryNotFoundException;
import com.example.fintrack.api.common.exception.EntityAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private static final String CACHE_NAME = "categories";
    private final CategoryRepository categoryRepository;

    public Category findByIdAndUserIdAndType(Long id, String userId, BillType type) {
        return categoryRepository.findByIdAndUserIdAndType(id, userId, type)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Cacheable(value = CACHE_NAME, key = "#userId + '-' + #type")
    public List<Category> findByUserIdAndType(String userId, BillType type) {
        return categoryRepository.findByUserIdAndTypeOrderByName(userId, type);
    }

    @CacheEvict(value = CACHE_NAME, key = "#userId + '-' + #category.type")
    public Category create(Category category, String userId) {
        category.create(userId);
        boolean exists = categoryRepository.existsByUserIdAndNameAndType(userId, category.getName(), category.getType());

        if(exists) {
            throw new EntityAlreadyExistsException("Category already exists");
        }

        return categoryRepository.save(category);
    }

}
