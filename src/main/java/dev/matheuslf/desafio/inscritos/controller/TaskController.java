package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.TaskRequest;
import dev.matheuslf.desafio.inscritos.dto.TaskResponse;
import dev.matheuslf.desafio.inscritos.entity.Project;
import dev.matheuslf.desafio.inscritos.entity.Task;
import dev.matheuslf.desafio.inscritos.enums.TaskPriority;
import dev.matheuslf.desafio.inscritos.enums.TaskStatus;
import dev.matheuslf.desafio.inscritos.mapper.ProjectMapper;
import dev.matheuslf.desafio.inscritos.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest request) {
        Project project = new Project(request.projectId());

        Task task = new Task();
        task.setProject(project);
        task.setTitle(request.title());
        task.setStatus(request.status());
        task.setPriority(request.priority());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());

        Task taskResponse = taskService.save(task);
        return ResponseEntity.ok(ProjectMapper.toTaskDTO(taskResponse));
    }

    @GetMapping
    public  ResponseEntity<List<TaskResponse>> findAll(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) UUID project
            ) {
        List<Task> tasks = taskService.findByFilters(status, priority, project);
        List<TaskResponse> response = tasks.stream()
                .map(ProjectMapper::toTaskDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateStatus(
            @PathVariable UUID id,
            @RequestBody TaskRequest request
    ) {
        Task updated = taskService.updateStatus(id, request.status());
        return ResponseEntity.ok(ProjectMapper.toTaskDTO(updated));
    }
}
