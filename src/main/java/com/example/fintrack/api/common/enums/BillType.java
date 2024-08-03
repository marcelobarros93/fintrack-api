package com.example.fintrack.api.common.enums;

import com.example.fintrack.api.common.exception.BusinessException;

public enum BillType {
    INCOME,
    EXPENSE;

    public static BillType fromString(String value) {
        try {
            return BillType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Invalid bill type");
        }
    }
}
