package org.example.depguard.project;

import org.example.depguard.mapper.ProjectMapper;
import org.example.depguard.scan.ScanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.example.depguard.dto.ProjectRequestDTO;
import org.example.depguard.dto.ProjectResponseDTO;


import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Transactional
    public ProjectResponseDTO createProject(ProjectRequestDTO request) {

        Project project = projectMapper.toEntity(request);

        Project savedProject = projectRepository.save(project);

        return projectMapper.toResponseDTO(savedProject);
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> getAllProjects(
            int page, int size, String sortBy, String direction) {

        if (page < 0 || size < 1 || size > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid pagination parameters");
        }

        if (sortBy == null ||
                !List.of("name", "createdAt").contains(sortBy)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid sort field");
        }

        if (direction == null ||
                (!direction.equalsIgnoreCase("asc") &&
                        !direction.equalsIgnoreCase("desc"))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid sort direction");
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return projectRepository.findByDeletedFalse(
                        PageRequest.of(page, size, sort))
                .map(projectMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public ProjectResponseDTO getProjectById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));

        if (project.isDeleted()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Project not found");
        }

        return projectMapper.toResponseDTO(project);
    }

    @Transactional
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));

        if (project.isDeleted()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Project not found");
        }

        project.setName(request.getName());
        project.setRepositoryUrl(request.getRepositoryUrl());

        return projectMapper.toResponseDTO(project);
    }

    @Transactional
    public void deleteProject(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));

        if (project.isDeleted()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Project not found");
        }

        project.setDeleted(true);

    }

    @Transactional
    public ProjectResponseDTO restoreProject(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));

        if (!project.isDeleted()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Project is already active");
        }

        project.setDeleted(false);

        return projectMapper.toResponseDTO(project);
    }
}

