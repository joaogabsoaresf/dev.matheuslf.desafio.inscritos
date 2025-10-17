package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.entity.Project;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAll() {
        return projectRepository.findByIsActiveTrue();
    }

    public Project save(Project project) {
        if (project.getStartDate() == null) {
            project.setStartDate(LocalDateTime.now());
        }
        return projectRepository.save(project);
    }
}
