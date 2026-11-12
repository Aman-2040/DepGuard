
package org.example.depguard.dto;

import org.example.depguard.scan.ScanStatus;

import java.time.LocalDateTime;

public class ScanResponseDTO {

    private final Long id;
    private final String status;
    private final LocalDateTime startedAt;
    private final LocalDateTime completedAt;
    private final Long projectId;
    private final LocalDateTime createdAt;
    public ScanResponseDTO(Long id, String status, LocalDateTime startedAt,
                           LocalDateTime completedAt, Long projectId, LocalDateTime createdAt) {
        this.id = id;
        this.status = status;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.projectId = projectId;
        this.createdAt = createdAt;
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
