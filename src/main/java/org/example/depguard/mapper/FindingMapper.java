package org.example.depguard.mapper;

import org.example.depguard.dto.FindingCreateDTO;
import org.example.depguard.dto.FindingResponseDTO;
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

    public static FindingResponseDTO toResponseDTO(Finding finding) {

        FindingResponseDTO dto = new FindingResponseDTO();

        dto.setId(finding.getId());
        dto.setTitle(finding.getTitle());
        dto.setDescription(finding.getDescription());
        dto.setSeverity(finding.getSeverity());
        dto.setDependencyName(finding.getDependencyName());
        dto.setInstalledVersion(finding.getInstalledVersion());
        dto.setFixedVersion(finding.getFixedVersion());
        dto.setVulnerabilityId(finding.getVulnerabilityId());
        dto.setCreatedAt(finding.getCreatedAt());
        dto.setScanId(finding.getScan().getId());

        return dto;
    }
}