package dev.matheuslf.desafio.inscritos.dto;

import java.time.LocalDate;
import java.util.UUID;
import dev.matheuslf.desafio.inscritos.enums.TaskPriority;
import dev.matheuslf.desafio.inscritos.enums.TaskStatus;

public record TaskRequest(
        String title,
        String description,
        TaskPriority priority,
        TaskStatus status,
        LocalDate dueDate,
        UUID projectId
) {}
