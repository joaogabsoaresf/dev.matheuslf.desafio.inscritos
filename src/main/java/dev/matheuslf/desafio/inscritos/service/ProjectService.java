package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.entity.Project;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final Validator validator;

    public ProjectService(ProjectRepository projectRepository, Validator validator) {
        this.projectRepository = projectRepository;
        this.validator = validator;
    }

    public List<Project> findAll() {
        return projectRepository.findByIsActiveTrue();
    }

    public Project save(Project project) {
        Set<ConstraintViolation<Project>> violations = validator.validate(project);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(", "))
            );
        }
        if (project.getStartDate() == null) {
            project.setStartDate(LocalDateTime.now());
        }

        return projectRepository.save(project);
    }
}
