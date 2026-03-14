package com.example.fintrack.api.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdateRequest(
        @NotBlank(message = "Name is required")
        String name
) {
}

