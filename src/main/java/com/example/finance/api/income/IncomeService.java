package com.example.finance.api.income;

import com.example.finance.api.common.enums.StatusType;
import com.example.finance.api.common.exception.BusinessException;
import com.example.finance.api.common.exception.IncomeNotFoundException;
import com.example.finance.api.planning.Planning;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public Income create(Income income) {
        income.setStatus(StatusType.OPEN);
        return incomeRepository.save(income);
    }

    public Income update(Long id, Income income) {
        if(income.isReceived() && income.getDateReceipt() == null) {
            throw new BusinessException("Date received is required when income is received.");
        }

        if(!income.isReceived()) {
            income.setDateReceipt(null);
        }

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

        if(income.isReceived()) {
            throw new BusinessException("This income has already been received");
        }

        income.setStatus(StatusType.RECEIVED);
        income.setDateReceipt(LocalDateTime.now());

        incomeRepository.save(income);
    }

    public void cancelReceipt(Long id) {
        Income income = findById(id);

        if(!income.isReceived()) {
            throw new BusinessException("This income is not received");
        }

        income.setStatus(StatusType.OPEN);
        income.setDateReceipt(null);

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
