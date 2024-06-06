package com.example.fintrack.api.category;

import com.example.fintrack.api.common.enums.BillType;
import com.example.fintrack.api.common.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Categories", description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/categories")
public class CategoryResource {

    private final SecurityUtils securityUtils;
    private final CategoryRepository categoryRepository;

    @Operation(summary = "Find categories by user id and type")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<CategoryResponse>> findByType(@PathVariable BillType type) {
        var userId = securityUtils.getUserId();
        var categories = categoryRepository.findByUserIdAndType(userId, type);
        return ResponseEntity.ok(categories.stream().map(this::toResponse).toList());
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getType());
    }
}
