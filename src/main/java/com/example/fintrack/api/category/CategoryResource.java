package com.example.fintrack.api.category;

import com.example.fintrack.api.common.enums.BillType;
import com.example.fintrack.api.common.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Categories", description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/categories")
public class CategoryResource {

    private final SecurityUtils securityUtils;
    private final CategoryService categoryService;

    @Operation(summary = "Find categories by user id and type")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<CategoryResponse>> findByType(@PathVariable String type) {
        var userId = securityUtils.getUserId();
        var categories = categoryService.findByUserIdAndType(userId, BillType.fromString(type));
        return ResponseEntity.ok(categories.stream().map(this::toResponse).toList());
    }

    @Operation(summary = "Create category")
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest request) {
        var category = categoryService.create(toEntity(request), securityUtils.getUserId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(category.getId())
                .toUri();

        return ResponseEntity.created(location).body(toResponse(category));
    }

    private Category toEntity(CategoryCreateRequest request) {
        var category = new Category();
        category.setName(request.name());
        category.setType(request.type());
        return category;
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getType());
    }
}
