package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.entity.Project;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void testSaveProjectSuccess() {
        Project project = new Project();
        project.setName("Teste");
        project.setDescription("Projeto de teste");
        project.setStartDate(LocalDateTime.now());
        project.setEndDate(LocalDateTime.now().plusDays(30));

        Project saved = projectService.save(project);

        assertNotNull(saved.getId());
        assertEquals("Teste", saved.getName());
    }

    @Test
    void testSaveProjectFail() {
        assertThrows(IllegalArgumentException.class, () -> {
            projectService.save(new Project());
        });
    }

    @Test
    void testSaveProjectWithNullStartDate() {
        Project project = new Project();
        project.setName("Teste");

        Project saved = projectService.save(project);

        assertNotNull(saved.getId());
        assertNotNull(saved.getStartDate());
    }

    @Test
    void testGetProjectsSuccess() {
        Project project = new Project();
        project.setName("Teste");
        projectService.save(project);

        List<Project> projects = projectService.findAll();
        assertNotNull(projects);
    }


}
