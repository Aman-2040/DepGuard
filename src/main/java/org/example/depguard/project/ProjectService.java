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
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ScanRepository scanRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper, ScanRepository scanRepository) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.scanRepository = scanRepository;
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

        return projectRepository.findByDeletedFalse(
                        PageRequest.of(page, size, sort))
                .map(projectMapper::toResponseDTO);
    }

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

        Project savedProject = projectRepository.save(project);

        return projectMapper.toResponseDTO(savedProject);
    }

    public void deleteProject(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));

        if (project.isDeleted()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Project not found");
        }

        project.setDeleted(true);

        projectRepository.save(project);
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

