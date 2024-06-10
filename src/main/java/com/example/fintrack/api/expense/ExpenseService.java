package com.example.fintrack.api.expense;

import com.example.fintrack.api.category.Category;
import com.example.fintrack.api.category.CategoryService;
import com.example.fintrack.api.common.enums.BillType;
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
    private final CategoryService categoryService;

    public Expense create(Expense expense, String userId) {
        expense.create(userId);
        expense.setCategory(getCategory(expense, userId));
        return expenseRepository.save(expense);
    }

    public Expense update(Long id, Expense expense, String userId) {
        var expenseSaved = findByIdAndUser(id, userId);
        expense.setStatus(expenseSaved.getStatus());
        expense.update();
        BeanUtils.copyProperties(expense, expenseSaved, "id", "userId", "planning", "status", "version", "createdAt", "updatedAt", "category");
        expenseSaved.setCategory(getCategory(expense, userId));
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

    public Expense buildByPlanning(Planning planning, LocalDate dateDue, Category category) {
        return Expense.builder()
                .status(StatusType.OPEN)
                .dateDue(dateDue)
                .description(planning.getBillName())
                .amount(planning.getAmount())
                .planning(planning)
                .category(category)
                .build();
    }

    private Category getCategory(Expense expense, String userId) {
        Category category = null;

        if(expense.getCategory() != null) {
            category = categoryService.findByIdAndUserIdAndType(
                    expense.getCategory().getId(), userId, BillType.EXPENSE);
        }

        return category;
    }

}
