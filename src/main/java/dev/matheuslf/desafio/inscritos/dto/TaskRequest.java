package dev.matheuslf.desafio.inscritos.dto;

import java.time.LocalDate;
import java.util.UUID;
import dev.matheuslf.desafio.inscritos.enums.TaskPriority;
import dev.matheuslf.desafio.inscritos.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequest(
        @NotNull(message = "O titulo da task é obrigatorio")
        @Size(min = 5, max = 150)
        String title,

        String description,

        TaskPriority priority,

        TaskStatus status,

        LocalDate dueDate,

        @NotNull(message = "O ID do projeto é obrigatório")
        UUID projectId
) {}
