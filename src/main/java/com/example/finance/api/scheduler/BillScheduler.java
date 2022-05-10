package com.example.finance.api.scheduler;

import com.example.finance.api.common.enums.BillType;
import com.example.finance.api.expense.Expense;
import com.example.finance.api.expense.ExpenseService;
import com.example.finance.api.income.Income;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class BillScheduler {

    private final PlanningRepository planningRepository;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @Scheduled(
            fixedRateString = "${scheduler.create.bill.fixed-rate}",
            initialDelayString = "${scheduler.create.bill.initial-delay}")
    @Transactional
    public void createByPlanning() {
        log.info("Start job create bills {}", LocalDateTime.now());

        List<Planning> plannings = planningRepository.findToCreateBill();

        List<Expense> expenses = new ArrayList<>();
        List<Income> incomes = new ArrayList<>();
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        Month month = now.getMonth();

        plannings.forEach(planning -> {
            if(planning.getType().equals(BillType.EXPENSE)) {
                Expense expense = expenseService.buildByPlanning(
                        planning, LocalDate.of(year, month, planning.getDueDay()));
                expenses.add(expense);
            } else {
                Income income = incomeService.buildByPlanning(
                        planning, LocalDate.of(year, month, planning.getDueDay()));
                incomes.add(income);
            }
        });

        if(!expenses.isEmpty()) {
            expenseService.create(expenses);
        }

        if(!incomes.isEmpty()) {
            incomeService.create(incomes);
        }

        log.info("Plannings {}", plannings.size());
        log.info("Incomes {}", incomes.size());
        log.info("Expenses {}", expenses.size());
        log.info("Finish job create bills {}", LocalDateTime.now());
    }
}
