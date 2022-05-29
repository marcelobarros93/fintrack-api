package com.example.finance.api.income;

import com.example.finance.api.common.enums.StatusType;
import com.example.finance.api.common.exception.IncomeNotFoundException;
import com.example.finance.api.planning.Planning;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public Income create(Income income) {
        income.create();
        return incomeRepository.save(income);
    }

    public Income update(Long id, Income income) {
        income.update();

        var incomeSaved = findById(id);
        BeanUtils.copyProperties(income, incomeSaved, "id", "status", "version", "createdAt", "updatedAt");
        return incomeRepository.save(incomeSaved);
    }

    public Income findById(Long id) {
        return incomeRepository.findById(id)
                .orElseThrow(() -> new IncomeNotFoundException(id));
    }

    public void receive(Long id) {
        Income income = findById(id);

        income.receive();

        incomeRepository.save(income);
    }

    public void cancelReceipt(Long id) {
        Income income = findById(id);

        income.cancelReceipt();

        incomeRepository.save(income);
    }

    public void deleteById(Long id) {
        boolean exists = incomeRepository.existsById(id);

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

    @Transactional
    public void create(List<Income> incomes) {
        incomes.forEach(this::create);
    }
}
