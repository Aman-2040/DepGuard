package org.example.depguard.mapper;

import org.example.depguard.dto.ScanResponseDTO;
import org.example.depguard.scan.Scan;

    public class ScanMapper {

        public static ScanResponseDTO toDTO(Scan scan) {
            return new ScanResponseDTO(
                    scan.getId(),
                    scan.getStatus(),
                    scan.getStartedAt(),
                    scan.getCompletedAt(),
                    scan.getProject().getId()
            );
        }
    }
