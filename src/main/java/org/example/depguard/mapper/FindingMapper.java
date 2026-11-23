package org.example.depguard.mapper;

import org.example.depguard.dto.FindingCreateDTO;
import org.example.depguard.dto.FindingResponseDTO;
import org.example.depguard.dto.FindingSummaryDTO;
import org.example.depguard.finding.Finding;
import org.example.depguard.finding.Severity;

import java.util.List;

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

    public static FindingSummaryDTO toSummaryDTO(List<Object[]> results) {

        FindingSummaryDTO dto = new FindingSummaryDTO();

        long critical = 0;
        long high = 0;
        long medium = 0;
        long low = 0;

        for (Object[] row : results) {
            Severity severity = (Severity) row[0];
            long count = ((Number) row[1]).longValue();

            switch (severity) {
                case CRITICAL -> critical = count;
                case HIGH -> high = count;
                case MEDIUM -> medium = count;
                case LOW -> low = count;
            }
        }

        dto.setCritical(critical);
        dto.setHigh(high);
        dto.setMedium(medium);
        dto.setLow(low);
        dto.setTotal(critical + high + medium + low);

        return dto;
    }

}