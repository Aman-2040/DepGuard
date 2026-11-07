package org.example.depguard.project;

import org.example.depguard.mapper.ProjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public Page<ProjectResponseDTO> getAllProjects(
            int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return projectRepository.findAll(PageRequest.of(page, size, sort))
                .map(projectMapper::toResponseDTO);
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

