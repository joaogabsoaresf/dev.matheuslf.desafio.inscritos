package dev.matheuslf.desafio.inscritos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.matheuslf.desafio.inscritos.entity.Project;
import dev.matheuslf.desafio.inscritos.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@Import(ProjectControllerTest.TestConfig.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectService projectService;

    static class TestConfig {
        @Bean
        ProjectService projectService() {
            return Mockito.mock(ProjectService.class);
        }
    }

    @BeforeEach
    void setup() {
        reset(projectService);
    }

    @Test
    void shouldReturnListOfProjects() throws Exception {
        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.setName("Teste Projeto");
        project.setDescription("Projeto de exemplo");
        project.setStartDate(LocalDateTime.now());

        when(projectService.findAll()).thenReturn(List.of(project));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Teste Projeto"))
                .andExpect(jsonPath("$[0].description").value("Projeto de exemplo"));

        verify(projectService, times(1)).findAll();
    }

    @Test
    void shouldCreateNewProject() throws Exception {
        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.setName("Novo Projeto");
        project.setDescription("Teste de criação");
        project.setStartDate(LocalDateTime.now());

        when(projectService.save(any(Project.class))).thenReturn(project);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Novo Projeto"))
                .andExpect(jsonPath("$.description").value("Teste de criação"));

        verify(projectService, times(1)).save(any(Project.class));
    }
}