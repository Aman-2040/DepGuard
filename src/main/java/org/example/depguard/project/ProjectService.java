package org.example.depguard.project;

import org.example.depguard.mapper.ProjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.example.depguard.dto.ProjectRequestDTO;
import org.example.depguard.dto.ProjectResponseDTO;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO request) {

        Project project = projectMapper.toEntity(request);

        Project savedProject = projectRepository.save(project);

        return projectMapper.toResponseDTO(savedProject);
    }

    public List<ProjectResponseDTO> getAllProjects() {

        return projectRepository.findAll()
                .stream()
                .map(projectMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProjectResponseDTO getProjectById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));

        return projectMapper.toResponseDTO(project);
    }

    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));

        project.setName(request.getName());
        project.setRepositoryUrl(request.getRepositoryUrl());

        Project savedProject = projectRepository.save(project);

        return projectMapper.toResponseDTO(savedProject);
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Project not found");
        }

        projectRepository.deleteById(id);
    }
}

