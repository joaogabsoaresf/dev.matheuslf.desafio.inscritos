package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.entity.Project;
import dev.matheuslf.desafio.inscritos.entity.Task;
import dev.matheuslf.desafio.inscritos.enums.TaskPriority;
import dev.matheuslf.desafio.inscritos.enums.TaskStatus;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TaskServiceTest {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private Project createProject() {
        Project project = new Project();
        project.setName("Projeto Teste");
        project.setStartDate(LocalDateTime.now());
        projectRepository.save(project);
        return project;
    }

    private Task createTask(Project project) {
        Task task = new Task();
        task.setTitle("Task Teste");
        task.setDescription("Descrição da task");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.MEDIUM);
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setProject(project);
        return taskService.save(task);
    }

    @Test
    void testSaveTaskSuccess() {
        Project project = createProject();
        Task task = createTask(project);

        assertNotNull(task.getId());
        assertEquals("Task Teste", task.getTitle());
        assertEquals(project.getId(), task.getProject().getId());
    }

    @Test
    void testSaveTaskFailWithoutProject() {
        Task task = new Task();
        task.setTitle("Task sem projeto");

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.save(task);
        });
    }

    @Test
    void testFindByFilters() {
        Project project = createProject();
        createTask(project);

        List<Task> tasksStatus = taskService.findByFilters(TaskStatus.TODO, null, null);
        assertEquals(1, tasksStatus.size());

        List<Task> tasksPriority = taskService.findByFilters(null, TaskPriority.MEDIUM, null);
        assertEquals(1, tasksPriority.size());

        List<Task> tasksProject = taskService.findByFilters(null, null, project.getId());
        assertEquals(1, tasksProject.size());

        List<Task> tasksAll = taskService.findByFilters(TaskStatus.TODO, TaskPriority.MEDIUM, project.getId());
        assertEquals(1, tasksAll.size());
    }

    @Test
    void testUpdateStatus() {
        Project project = createProject();
        Task task = createTask(project);

        Task updated = taskService.updateStatus(task.getId(), TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, updated.getStatus());
    }

    @Test
    void testDeleteTask() {
        Project project = createProject();
        Task task = createTask(project);

        taskService.delete(task.getId());

        Task deleted = taskRepository.findById(task.getId()).orElseThrow();
        assertFalse(deleted.isActive());
    }
}
