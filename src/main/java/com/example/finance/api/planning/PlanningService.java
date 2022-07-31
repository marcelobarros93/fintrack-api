package com.example.finance.api.planning;

import com.example.finance.api.common.exception.PlanningNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanningService {

    private final PlanningRepository planningRepository;

    public Planning create(Planning planning, String userId) {
        planning.create(userId);
        return planningRepository.save(planning);
    }

    public Planning update(Long id, Planning planning, String userId) {
        planning.update();
        var planningSaved = findByIdAndUser(id, userId);
        BeanUtils.copyProperties(planning, planningSaved,
                "active", "id", "version", "createdAt", "updatedAt");
        return planningRepository.save(planningSaved);
    }

    public Planning findByIdAndUser(Long id, String userId) {
        return planningRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new PlanningNotFoundException(id));
    }

    public void activateById(Long id, String userId) {
        var planning = findByIdAndUser(id, userId);

        planning.activate();

        planningRepository.save(planning);
    }

    public void inactivateById(Long id, String userId) {
        var planning = findByIdAndUser(id, userId);

        planning.inactivate();

        planningRepository.save(planning);
    }

    public void deleteById(Long id, String userId) {
        boolean exists = planningRepository.existsByIdAndUserId(id, userId);

        if(!exists) {
            throw new PlanningNotFoundException(id);
        }

        planningRepository.deleteById(id);
    }

}
