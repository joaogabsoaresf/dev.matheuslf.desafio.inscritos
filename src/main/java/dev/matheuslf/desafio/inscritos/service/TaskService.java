package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.entity.Project;
import dev.matheuslf.desafio.inscritos.entity.Task;
import dev.matheuslf.desafio.inscritos.enums.TaskPriority;
import dev.matheuslf.desafio.inscritos.enums.TaskStatus;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final Validator validator;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, Validator validator, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.validator = validator;
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public List<Task> findByFilters(TaskStatus status, TaskPriority priority, UUID projectId) {
        Specification<Task> spec = Specification.allOf(
                (root, query, cb) -> cb.isTrue(root.get("isActive")),
                status == null ? null : (root, query, cb) -> cb.equal(root.get("status"), status),
                priority == null ? null : (root, query, cb) -> cb.equal(root.get("priority"), priority),
                projectId == null ? null : (root, query, cb) -> cb.equal(root.get("project").get("id"), projectId)
        );


        return taskRepository.findAll(spec);
    }

    public Task save(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }

        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(", "))
            );
        }

        if (task.getProject() == null || task.getProject().getId() == null) {
            throw new IllegalArgumentException("A tarefa deve estar vinculada a um projeto");
        }
        Project project = projectRepository.findById(task.getProject().getId())
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        task.setProject(project);
        return taskRepository.save(task);
    }

    @Transactional()
    public Task updateStatus(UUID id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));
        assert task != null;
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Transactional()
    public void delete(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));
        assert task != null;
        task.setActive(false);
        taskRepository.save(task);
    }
}
