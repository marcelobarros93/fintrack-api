package com.example.fintrack.api.planning;

import com.example.fintrack.api.category.Category;
import com.example.fintrack.api.category.CategoryService;
import com.example.fintrack.api.common.exception.EntityInUseException;
import com.example.fintrack.api.common.exception.PlanningNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanningService {

    private final PlanningRepository planningRepository;
    private final CategoryService categoryService;

    public Planning create(Planning planning, String userId) {
        planning.create(userId);
        planning.setCategory(getCategory(planning, userId));
        return planningRepository.save(planning);
    }

    public Planning update(Long id, Planning planning, String userId) {
        planning.update();
        var planningSaved = findByIdAndUser(id, userId);
        BeanUtils.copyProperties(planning, planningSaved,
                "id", "userId", "version", "createdAt", "updatedAt", "category");
        planningSaved.setCategory(getCategory(planning, userId));
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

        try {
            planningRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException("This planning has other associated records and cannot be deleted");
        }
    }

    private Category getCategory(Planning planning, String userId) {
        Category category = null;

        if(planning.getCategory() != null) {
            category = categoryService.findByIdAndUserIdAndType(
                    planning.getCategory().getId(), userId, planning.getType());
        }

        return category;
    }

}
