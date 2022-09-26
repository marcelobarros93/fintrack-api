package com.example.finance.api.statistics;

import com.example.finance.api.common.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@Tag(name = "Dashboard", description = "Endpoints for managing charts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/statistics")
public class StatisticsResource {

    private final StatisticsService statisticsService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "Get balance statistics")
    @GetMapping("/balance")
    public ResponseEntity<Balance> getBalance(@RequestParam YearMonth month) {
        var balance = statisticsService.getBalance(month, securityUtils.getUserId());
        return ResponseEntity.ok(balance);
    }

    @Operation(summary = "Get period overview statistics")
    @GetMapping("/period-overview")
    public ResponseEntity<PeriodOverview> getPeriodOverview(
            @RequestParam YearMonth start,
            @RequestParam YearMonth end) {
        var overview = statisticsService.getPeriodOverview(
                start, end, securityUtils.getUserId());
        return ResponseEntity.ok(overview);
    }
}
