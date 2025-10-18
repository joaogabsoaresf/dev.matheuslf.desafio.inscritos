package dev.matheuslf.desafio.inscritos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<TaskResponse> tasks
) {
}
