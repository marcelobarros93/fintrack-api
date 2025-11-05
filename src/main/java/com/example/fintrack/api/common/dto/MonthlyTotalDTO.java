package com.example.fintrack.api.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class MonthlyTotalDTO implements Serializable {
    private String month;
    private BigDecimal total;
}
