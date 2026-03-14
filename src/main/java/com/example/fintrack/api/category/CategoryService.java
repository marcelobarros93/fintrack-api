package com.example.fintrack.api.category;

import com.example.fintrack.api.common.cache.CacheName;
import com.example.fintrack.api.common.enums.BillType;
import com.example.fintrack.api.common.exception.CategoryNotFoundException;
import com.example.fintrack.api.common.exception.EntityAlreadyExistsException;
import com.example.fintrack.api.common.exception.EntityInUseException;
import com.example.fintrack.api.expense.ExpenseRepository;
import com.example.fintrack.api.income.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public Category findByIdAndUserId(Long id, String userId) {
        return categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public Category findByIdAndUserIdAndType(Long id, String userId, BillType type) {
        return categoryRepository.findByIdAndUserIdAndType(id, userId, type)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Cacheable(value = CacheName.CATEGORIES, key = "#userId + '-' + #type", unless = "#result == null or #result.size() == 0")
    public List<Category> findByUserIdAndType(String userId, BillType type) {
        return categoryRepository.findByUserIdAndTypeOrderByName(userId, type);
    }

    @CacheEvict(value = CacheName.CATEGORIES, key = "#userId + '-' + #category.type")
    public Category create(Category category, String userId) {
        category.create(userId);
        boolean exists = categoryRepository.existsByUserIdAndNameAndType(userId, category.getName(), category.getType());

        if(exists) {
            throw new EntityAlreadyExistsException("Category already exists");
        }

        return categoryRepository.save(category);
    }

    @CacheEvict(value = CacheName.CATEGORIES, allEntries = true)
    public Category update(Long id, CategoryUpdateRequest request, String userId) {
        var category = findByIdAndUserId(id, userId);

        boolean exists = categoryRepository.existsByUserIdAndNameAndType(userId, request.name(), category.getType());
        if(exists && !category.getName().equalsIgnoreCase(request.name())) {
            throw new EntityAlreadyExistsException("Category with this name already exists");
        }

        category.setName(request.name());
        return categoryRepository.save(category);
    }

    @CacheEvict(value = CacheName.CATEGORIES, allEntries = true)
    public void delete(Long id, String userId) {
        var category = findByIdAndUserId(id, userId);

        if(expenseRepository.existsByCategoryId(id) || incomeRepository.existsByCategoryId(id)) {
            throw new EntityInUseException("Category is in use and cannot be deleted");
        }

        categoryRepository.delete(category);
    }

    @CacheEvict(value = CacheName.CATEGORIES, allEntries = true)
    public Category toggleActive(Long id, String userId) {
        var category = findByIdAndUserId(id, userId);
        category.toggleActive();
        return categoryRepository.save(category);
    }
}


