package dev.matheuslf.desafio.inscritos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ProjectRequest(
        @Size(min = 3, max = 100)
        @NotBlank(message = "O nome do projeto não pode estar vazio")
        String name,

        String description,

        LocalDateTime startDate,

        LocalDateTime endDate
) {
}
