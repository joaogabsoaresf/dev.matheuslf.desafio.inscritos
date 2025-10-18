package dev.matheuslf.desafio.inscritos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.matheuslf.desafio.inscritos.dto.TaskRequest;
import dev.matheuslf.desafio.inscritos.entity.Project;
import dev.matheuslf.desafio.inscritos.entity.Task;
import dev.matheuslf.desafio.inscritos.enums.TaskPriority;
import dev.matheuslf.desafio.inscritos.enums.TaskStatus;
import dev.matheuslf.desafio.inscritos.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@Import(TaskControllerTest.TestConfig.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskService taskService;

    static class TestConfig {
        @Bean
        TaskService taskService() {
            return Mockito.mock(TaskService.class);
        }
    }



    @BeforeEach
    void setup() {
        reset(taskService);
    }

    @Test
    void shouldReturnListOfTasks() throws Exception {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle("Implementar autenticação");
        task.setDescription("Implementar login com JWT");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setProject(new Project(UUID.randomUUID()));

        when(taskService.findByFilters(any(), any(), any())).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Implementar autenticação"))
                .andExpect(jsonPath("$[0].status").value("TODO"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"));

        verify(taskService, times(1)).findByFilters(any(), any(), any());
    }

    @Test
    void shouldCreateNewTask() throws Exception {
        UUID projectId = UUID.randomUUID();
        TaskRequest request = new TaskRequest(
                        "Nova tarefa",
                        "Descrição da tarefa",
                        TaskPriority.MEDIUM,
                TaskStatus.TODO,
                LocalDate.now().plusDays(3),
                projectId
                );

        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority());
        task.setStatus(request.status());
        task.setDueDate(request.dueDate());
        task.setProject(new Project(projectId));

        when(taskService.save(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Nova tarefa"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.status").value("TODO"));

        verify(taskService, times(1)).save(any(Task.class));
    }

    @Test
    void shouldUpdateTaskStatus() throws Exception {
        UUID taskId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();

        Project project = new Project();
        project.setId(projectId);
        project.setName("Projeto de teste");

        Task updated = new Task();
        updated.setId(taskId);
        updated.setTitle("Task Atualizada");
        updated.setStatus(TaskStatus.DONE);
        updated.setProject(project);

        when(taskService.updateStatus(eq(taskId), eq(TaskStatus.DONE)))
                .thenReturn(updated);

        TaskRequest request = new TaskRequest(
                        null, null, null, TaskStatus.DONE, null, null
                );

        mockMvc.perform(put("/api/tasks/" + taskId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));

        verify(taskService, times(1)).updateStatus(eq(taskId), eq(TaskStatus.DONE));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        UUID taskId = UUID.randomUUID();

        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).delete(taskId);
    }
}
