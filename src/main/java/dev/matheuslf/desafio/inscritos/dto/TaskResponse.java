package dev.matheuslf.desafio.inscritos.dto;

import dev.matheuslf.desafio.inscritos.enums.TaskPriority;
import dev.matheuslf.desafio.inscritos.enums.TaskStatus;

import java.time.LocalDate;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        Enum<TaskStatus> status,
        Enum<TaskPriority> priority,
        LocalDate dueDate,
        UUID project
) {}
