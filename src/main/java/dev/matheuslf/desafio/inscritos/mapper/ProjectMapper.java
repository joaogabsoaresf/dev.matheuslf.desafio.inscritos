package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.TaskResponse;
import dev.matheuslf.desafio.inscritos.dto.ProjectResponse;
import dev.matheuslf.desafio.inscritos.entity.Task;
import dev.matheuslf.desafio.inscritos.entity.Project;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectMapper {

    public static ProjectResponse toProjectDTO(Project project) {
        List<TaskResponse> taskDTOs = project.getTasks() != null
                ? project.getTasks().stream()
                .map(ProjectMapper::toTaskDTO)
                .collect(Collectors.toList())
                :List.of();

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate(),
                taskDTOs
        );
    }

    public static TaskResponse toTaskDTO(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getProject().getId()
        );
    }
}
