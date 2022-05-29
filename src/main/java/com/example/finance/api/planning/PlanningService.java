package com.example.finance.api.planning;

import com.example.finance.api.common.exception.PlanningNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanningService {

    private final PlanningRepository planningRepository;

    public Planning create(Planning planning) {
        planning.create();
        return planningRepository.save(planning);
    }

    public Planning update(Long id, Planning planning) {
        planning.update();
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

        planning.activate();

        planningRepository.save(planning);
    }

    public void inactivateById(Long id) {
        var planning = findById(id);

        planning.inactivate();

        planningRepository.save(planning);
    }

    public void deleteById(Long id) {
        boolean exists = planningRepository.existsById(id);

        if(!exists) {
            throw new PlanningNotFoundException(id);
        }

        planningRepository.deleteById(id);
    }

}
