package com.example.fintrack.api.user;

public record UserResponse(
        String id,
        String name,
        String email
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}

