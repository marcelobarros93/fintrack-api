package com.example.finance.api.scheduler;

import com.example.finance.api.common.enums.BillType;
import com.example.finance.api.expense.ExpenseService;
import com.example.finance.api.income.IncomeService;
import com.example.finance.api.planning.Planning;
import com.example.finance.api.planning.PlanningRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
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
        log.info("Start job create bills {}", LocalDateTime.now());

        List<Planning> plannings = planningRepository.findToCreateBill();

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        Month month = now.getMonth();
        int lastDayOfMonth = month.length(now.isLeapYear());

        plannings.forEach(planning -> {
            int dueDay = planning.getDueDay() > lastDayOfMonth ? lastDayOfMonth : planning.getDueDay();

            if(planning.getType().equals(BillType.EXPENSE)) {
                var expense = expenseService.buildByPlanning(
                        planning, LocalDate.of(year, month, dueDay));
                expenseService.create(expense, planning.getUserId());
            } else {
                var income = incomeService.buildByPlanning(
                        planning, LocalDate.of(year, month, dueDay));
                incomeService.create(income, planning.getUserId());
            }
        });

        log.info("Finish job create bills {}", LocalDateTime.now());
    }
}