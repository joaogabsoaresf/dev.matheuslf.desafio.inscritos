package dev.matheuslf.desafio.inscritos.repository;

import dev.matheuslf.desafio.inscritos.entity.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void findByIsActiveTrue() {
        Project project = new Project();
        project.setName("test");
        project.setStartDate(LocalDateTime.now());
        projectRepository.save(project);

        Project project2 = new Project();
        project2.setName("test2");
        project2.setStartDate(LocalDateTime.now());
        project2.setActive(false);
        projectRepository.save(project);

        List<Project> projects = projectRepository.findByIsActiveTrue();

        assertEquals(1, projects.size());
    }
}
