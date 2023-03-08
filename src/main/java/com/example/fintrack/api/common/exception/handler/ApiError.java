package com.example.fintrack.api.common.exception.handler;

import java.time.Instant;
import java.util.List;

public record ApiError(
        Integer code,
        List<String> messages,
        Instant timestamp) {
}
