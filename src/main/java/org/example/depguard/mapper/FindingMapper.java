package org.example.depguard.mapper;

import org.example.depguard.dto.FindingCreateDTO;
import org.example.depguard.finding.Finding;

public class FindingMapper {

    public static Finding toEntity(FindingCreateDTO request) {
        return new Finding(
                request.getTitle(),
                request.getDescription(),
                request.getSeverity(),
                request.getDependencyName(),
                request.getInstalledVersion(),
                request.getFixedVersion(),
                request.getVulnerabilityId()
        );
    }
}