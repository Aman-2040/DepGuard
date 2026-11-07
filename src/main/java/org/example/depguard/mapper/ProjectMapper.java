package org.example.depguard.mapper;

import org.example.depguard.dto.ProjectRequestDTO;
import org.example.depguard.dto.ProjectResponseDTO;
import org.example.depguard.project.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    public ProjectResponseDTO toResponseDTO(Project project) {

        ProjectResponseDTO dto = new ProjectResponseDTO();

        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setRepositoryUrl(project.getRepositoryUrl());
        dto.setCreatedAt(project.getCreatedAt());

        return dto;
    }
    public Project toEntity(ProjectRequestDTO request) {

        Project project = new Project();

        project.setName(request.getName());
        project.setRepositoryUrl(request.getRepositoryUrl());

        return project;
    }
}
