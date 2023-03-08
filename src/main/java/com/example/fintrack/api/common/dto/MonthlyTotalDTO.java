package com.example.fintrack.api.common.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyTotalDTO {
    private String month;
    private BigDecimal total;
}
