package com.example.fintrack.api.common.security;

import com.example.fintrack.api.common.exception.BusinessException;
import com.example.fintrack.api.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public String getUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    public void validateLoggedUser(String userId) {
        if (!getUserId().equals(userId)) {
            throw new BusinessException("Invalid user id");
        }
    }
}
