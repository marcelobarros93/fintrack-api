package com.example.fintrack.api.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryCreateRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Type is required")
        String type
) {
}
