package com.example.fintrack.api.category;

import com.example.fintrack.api.common.enums.BillType;

public record CategoryResponse(Long id, String name, BillType type) {
}
