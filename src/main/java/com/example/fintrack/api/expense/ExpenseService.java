package com.example.fintrack.api.expense;

import com.example.fintrack.api.common.enums.StatusType;
import com.example.fintrack.api.common.exception.ExpenseNotFoundException;
import com.example.fintrack.api.planning.Planning;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public Expense create(Expense expense, String userId) {
        expense.create(userId);
        return expenseRepository.save(expense);
    }

    public Expense update(Long id, Expense expense, String userId) {
        expense.update();
        var expenseSaved = findByIdAndUser(id, userId);
        BeanUtils.copyProperties(expense, expenseSaved, "id", "userId", "planning", "status", "version", "createdAt", "updatedAt");
        return expenseRepository.save(expenseSaved);
    }

    public Expense findByIdAndUser(Long id, String userId) {
        return expenseRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ExpenseNotFoundException(id));
    }

    public void pay(Long id, String userId) {
        Expense expense = findByIdAndUser(id, userId);

        expense.pay();

        expenseRepository.save(expense);
    }

    public void cancelPayment(Long id, String userId) {
        Expense expense = findByIdAndUser(id, userId);

        expense.cancelPayment();

        expenseRepository.save(expense);
    }

    public void deleteById(Long id, String userId) {
        boolean exists = expenseRepository.existsByIdAndUserId(id, userId);

        if(!exists) {
            throw new ExpenseNotFoundException(id);
        }

        expenseRepository.deleteById(id);
    }

    public Expense buildByPlanning(Planning planning, LocalDate dateDue) {
        return Expense.builder()
                .status(StatusType.OPEN)
                .dateDue(dateDue)
                .description(planning.getDescription())
                .amount(planning.getAmount())
                .planning(planning)
                .build();
    }

}
