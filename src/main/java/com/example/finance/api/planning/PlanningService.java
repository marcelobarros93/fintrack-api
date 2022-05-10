package com.example.finance.api.planning;

import com.example.finance.api.common.exception.BusinessException;
import com.example.finance.api.common.exception.PlanningNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PlanningService {

    private final PlanningRepository planningRepository;

    public Planning create(Planning planning) {
        validatePlanningDates(planning);
        planning.setActive(Boolean.TRUE);
        return planningRepository.save(planning);
    }

    public Planning update(Long id, Planning planning) {
        validatePlanningDates(planning);
        var planningSaved = findById(id);
        BeanUtils.copyProperties(planning, planningSaved,
                "active", "id", "version", "createdAt", "updatedAt");
        return planningRepository.save(planningSaved);
    }

    public Planning findById(Long id) {
        return planningRepository.findById(id)
                .orElseThrow(() -> new PlanningNotFoundException(id));
    }

    public void activateById(Long id) {
        var planning = findById(id);

        if(Boolean.TRUE.equals(planning.getActive())) {
            throw new BusinessException("This planning is not inactive");
        }

        planning.setActive(Boolean.TRUE);
        planningRepository.save(planning);
    }

    public void inactivateById(Long id) {
        var planning = findById(id);

        if(Boolean.FALSE.equals(planning.getActive())) {
            throw new BusinessException("This planning is not active");
        }

        planning.setActive(Boolean.FALSE);
        planningRepository.save(planning);
    }

    public void deleteById(Long id) {
        boolean exists = planningRepository.existsById(id);

        if(!exists) {
            throw new PlanningNotFoundException(id);
        }

        planningRepository.deleteById(id);
    }

    private void validatePlanningDates(Planning planning) {
        if(planning.getStartAt().isAfter(planning.getEndAt())) {
            throw new BusinessException("The date start cannot be greater than the date end");
        }

        if(planning.getEndAt().isBefore(LocalDate.now())) {
            throw new BusinessException("The date end cannot be less than today");
        }
    }
}
