package com.example.fintrack.api.common.config;

import com.example.fintrack.api.common.dto.MonthlyTotalDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.ColumnResult;

@Entity
@SqlResultSetMapping(
    name = "MonthlyTotalDTOMapping",
    classes = @ConstructorResult(
        targetClass = MonthlyTotalDTO.class,
        columns = {
            @ColumnResult(name = "month", type = String.class),
            @ColumnResult(name = "total", type = java.math.BigDecimal.class)
        }
    )
)
public class SqlMappingConfiguration {
    @Id
    private Long id;
}