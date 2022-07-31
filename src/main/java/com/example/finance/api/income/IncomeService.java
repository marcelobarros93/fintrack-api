package com.example.finance.api.income;

import com.example.finance.api.common.enums.StatusType;
import com.example.finance.api.common.exception.IncomeNotFoundException;
import com.example.finance.api.planning.Planning;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public Income create(Income income, String userId) {
        income.create(userId);
        return incomeRepository.save(income);
    }

    public Income update(Long id, Income income, String userId) {
        income.update();

        var incomeSaved = findByIdAndUser(id, userId);
        BeanUtils.copyProperties(income, incomeSaved, "id", "status", "version", "createdAt", "updatedAt");
        return incomeRepository.save(incomeSaved);
    }

    public Income findByIdAndUser(Long id, String userId) {
        return incomeRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IncomeNotFoundException(id));
    }

    public void receive(Long id, String userId) {
        Income income = findByIdAndUser(id, userId);

        income.receive();

        incomeRepository.save(income);
    }

    public void cancelReceipt(Long id, String userId) {
        Income income = findByIdAndUser(id, userId);

        income.cancelReceipt();

        incomeRepository.save(income);
    }

    public void deleteById(Long id, String userId) {
        boolean exists = incomeRepository.existsByIdAndUserId(id, userId);

        if(!exists) {
            throw new IncomeNotFoundException(id);
        }

        incomeRepository.deleteById(id);
    }

    public Income buildByPlanning(Planning planning, LocalDate dateDue) {
        return Income.builder()
                .status(StatusType.OPEN)
                .dateDue(dateDue)
                .description(planning.getDescription())
                .amount(planning.getAmount())
                .planning(planning)
                .build();
    }

}
