package org.example.depguard.project;

import org.example.depguard.dto.ProjectRequestDTO;
import org.example.depguard.dto.ProjectResponseDTO;
import org.example.depguard.mapper.ProjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProjectServiceTest {
    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectService = new ProjectService(
                projectRepository,
                new ProjectMapper()
        );
    }

    @Test
    public void getProjectById_whenProjectExists_returnsProject() {
        Project project = new Project(
                "DepGuard",
                "https://github.com/example/depguard"
        );

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        ProjectResponseDTO result =
                projectService.getProjectById(1L);

        assertEquals("DepGuard", result.getName());
        assertEquals(
                "https://github.com/example/depguard",
                result.getRepositoryUrl()
        );
    }

    @Test
    public void getProjectById_whenProjectDoesNotExist_throwsNotFound() {
        when(projectRepository.findById(500L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> projectService.getProjectById(500L)
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void getProjectById_whenProjectIsDeleted_throwsNotFound() {
        Project project = new Project(
                "DepGuard",
                "https://github.com/example/depguard"
        );
        project.setDeleted(true);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        assertThrows(
                ResponseStatusException.class,
                () -> projectService.getProjectById(1L)
        );
    }

    @Test
    void getAllProjects_withValidParameters_returnsProjects() {
        Project project = new Project(
                "DepGuard",
                "https://github.com/example/depguard"
        );

        Page<Project> projects = new PageImpl<>(List.of(project));

        when(projectRepository.findByDeletedFalse(any(Pageable.class)))
                .thenReturn(projects);

        Page<ProjectResponseDTO> result =
                projectService.getAllProjects(0, 10, "name", "asc");

        assertEquals(1, result.getTotalElements());
        assertEquals("DepGuard", result.getContent().get(0).getName());
    }

    @Test
    void getAllProjects_withInvalidParameters_throwsBadRequest() {
        assertThrows(ResponseStatusException.class,
                () -> projectService.getAllProjects(-1, 10, "name", "asc"));

        assertThrows(ResponseStatusException.class,
                () -> projectService.getAllProjects(0, 0, "name", "asc"));

        assertThrows(ResponseStatusException.class,
                () -> projectService.getAllProjects(0, 10, "invalidField", "asc"));

        assertThrows(ResponseStatusException.class,
                () -> projectService.getAllProjects(0, 10, "name", "invalidDirection"));
    }

    @Test
    void updateProject_whenProjectExists_updatesProjectDetails() {
        Project project = new Project(
                "OldName",
                "https://github.com/example/old"
        );

        ProjectRequestDTO request = new ProjectRequestDTO();
        request.setName("NewName");
        request.setRepositoryUrl("https://github.com/example/new");

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        ProjectResponseDTO result = projectService.updateProject(1L, request);

        assertEquals("NewName", result.getName());
        assertEquals("https://github.com/example/new", result.getRepositoryUrl());
    }

    @Test
    void updateProject_whenProjectIsMissingOrDeleted_throwsNotFound() {
        ProjectRequestDTO request = new ProjectRequestDTO();
        request.setName("NewName");
        request.setRepositoryUrl("https://github.com/example/new");

        when(projectRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> projectService.updateProject(1L, request));

        Project deletedProject = new Project("OldName", "https://github.com/example/old");
        deletedProject.setDeleted(true);

        when(projectRepository.findById(2L))
                .thenReturn(Optional.of(deletedProject));

        assertThrows(ResponseStatusException.class,
                () -> projectService.updateProject(2L, request));
    }

    @Test
    void deleteProject_whenProjectExists_softDeletesProject() {
        Project project = new Project("Test", "https://github.com/example/test");

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        projectService.deleteProject(1L);

        assertTrue(project.isDeleted());
    }

    @Test
    void deleteProject_whenProjectIsMissingOrAlreadyDeleted_throwsNotFound() {
        when(projectRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> projectService.deleteProject(1L));

        Project deletedProject = new Project("Old", "https://github.com/example/old");
        deletedProject.setDeleted(true);

        when(projectRepository.findById(2L))
                .thenReturn(Optional.of(deletedProject));

        assertThrows(ResponseStatusException.class,
                () -> projectService.deleteProject(2L));
    }

    @Test
    void restoreProject_whenProjectIsDeleted_restoresProject() {
        Project project = new Project("Test", "https://github.com/example/test");
        project.setDeleted(true);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        projectService.restoreProject(1L);

        assertFalse(project.isDeleted());
    }

    @Test
    void restoreProject_whenProjectIsMissingOrAlreadyActive_throwsException() {
        when(projectRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> projectService.restoreProject(1L));

        Project activeProject = new Project("Active", "https://github.com/example/active");

        when(projectRepository.findById(2L))
                .thenReturn(Optional.of(activeProject));

        assertThrows(ResponseStatusException.class,
                () -> projectService.restoreProject(2L));
    }

}
