package com.example.fintrack.api.scheduler;

import com.example.fintrack.api.common.enums.BillType;
import com.example.fintrack.api.expense.ExpenseService;
import com.example.fintrack.api.income.IncomeService;
import com.example.fintrack.api.planning.Planning;
import com.example.fintrack.api.planning.PlanningRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateBillScheduler {

    private final PlanningRepository planningRepository;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @Scheduled(
            fixedRateString = "${scheduler.create.bill.fixed-rate}",
            initialDelayString = "${scheduler.create.bill.initial-delay}")
    @Transactional
    public void execute() {
        log.debug("Start job create bills {}", LocalDateTime.now());

        LocalDate now = LocalDate.now();
        createBillsForMonth(now);
        createBillsForMonth(now.plusMonths(1));

        log.debug("Finish job create bills {}", LocalDateTime.now());
    }

    private void createBillsForMonth(LocalDate referenceDate) {
        List<Planning> plannings = planningRepository.findToCreateBill(referenceDate);

        int year = referenceDate.getYear();
        Month month = referenceDate.getMonth();
        int lastDayOfMonth = month.length(referenceDate.isLeapYear());

        plannings.forEach(planning -> {
            int dueDay = planning.getDueDay() > lastDayOfMonth ? lastDayOfMonth : planning.getDueDay();

            if (planning.getType().equals(BillType.EXPENSE)) {
                var expense = expenseService.buildByPlanning(
                        planning, LocalDate.of(year, month, dueDay), planning.getCategory());
                expenseService.create(expense, planning.getUserId());
            } else {
                var income = incomeService.buildByPlanning(
                        planning, LocalDate.of(year, month, dueDay), planning.getCategory());
                incomeService.create(income, planning.getUserId());
            }
        });
    }
}
