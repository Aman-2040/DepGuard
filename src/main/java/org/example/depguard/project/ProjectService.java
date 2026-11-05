package org.example.depguard.project;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.example.depguard.dto.ProjectRequestDTO;
import org.example.depguard.dto.ProjectResponseDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO request) {

        Project project = new Project();

        project.setName(request.getName());
        project.setRepositoryUrl(request.getRepositoryUrl());

        Project savedProject = projectRepository.save(project);

        ProjectResponseDTO response = new ProjectResponseDTO();

        response.setId(savedProject.getId());
        response.setName(savedProject.getName());
        response.setRepositoryUrl(savedProject.getRepositoryUrl());
        response.setCreatedAt(savedProject.getCreatedAt());

        return response;
    }

    public List<ProjectResponseDTO> getAllProjects() {

        List<Project> projects = projectRepository.findAll();

        List<ProjectResponseDTO> response = new ArrayList<>();

        for (Project project : projects) {

            ProjectResponseDTO dto = new ProjectResponseDTO();

            dto.setId(project.getId());
            dto.setName(project.getName());
            dto.setRepositoryUrl(project.getRepositoryUrl());
            dto.setCreatedAt(project.getCreatedAt());

            response.add(dto);
        }

        return response;
    }

    public ProjectResponseDTO getProjectById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));

        ProjectResponseDTO response = new ProjectResponseDTO();

        response.setId(project.getId());
        response.setName(project.getName());
        response.setRepositoryUrl(project.getRepositoryUrl());
        response.setCreatedAt(project.getCreatedAt());

        return response;
    }

    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));

        project.setName(request.getName());
        project.setRepositoryUrl(request.getRepositoryUrl());

        Project savedProject = projectRepository.save(project);

        ProjectResponseDTO response = new ProjectResponseDTO();

        response.setId(savedProject.getId());
        response.setName(savedProject.getName());
        response.setRepositoryUrl(savedProject.getRepositoryUrl());
        response.setCreatedAt(savedProject.getCreatedAt());

        return response;
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Project not found");
        }

        projectRepository.deleteById(id);
    }

}
