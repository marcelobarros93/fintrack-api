package com.example.finance.api.common.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyTotalDTO {
    private String month;
    private BigDecimal total;
}
