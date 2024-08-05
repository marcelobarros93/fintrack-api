package com.example.fintrack.api.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MonthlyTotalDTO implements Serializable {
    private String month;
    private BigDecimal total;
}
