
package org.example.depguard.dto;

import java.time.LocalDateTime;

public class ScanResponseDTO {

    private final Long id;
    private final String status;
    private final LocalDateTime startedAt;
    private final LocalDateTime completedAt;
    private final Long projectId;

    public ScanResponseDTO(Long id, String status, LocalDateTime startedAt,
                           LocalDateTime completedAt, Long projectId) {
        this.id = id;
        this.status = status;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.projectId = projectId;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public Long getProjectId() {
        return projectId;
    }
}
