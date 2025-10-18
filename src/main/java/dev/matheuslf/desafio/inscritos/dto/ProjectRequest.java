package dev.matheuslf.desafio.inscritos.dto;

import java.time.LocalDateTime;

public record ProjectRequest(
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
