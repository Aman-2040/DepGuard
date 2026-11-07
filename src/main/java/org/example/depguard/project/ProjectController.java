package org.example.depguard.project;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.depguard.dto.ProjectRequestDTO;
import org.example.depguard.dto.ProjectResponseDTO;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(
            @Valid @RequestBody ProjectRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(request));
    }

    @GetMapping
    public Page<ProjectResponseDTO> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return projectService.getAllProjects(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    public ProjectResponseDTO getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PutMapping("/{id}")
    public ProjectResponseDTO updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequestDTO request) {

        return projectService.updateProject(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
