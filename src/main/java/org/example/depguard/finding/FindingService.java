package org.example.depguard.finding;

import org.example.depguard.dto.FindingCreateDTO;
import org.example.depguard.dto.FindingResponseDTO;
import org.example.depguard.mapper.FindingMapper;
import org.example.depguard.scan.Scan;
import org.example.depguard.scan.ScanStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.example.depguard.scan.ScanRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FindingService {

    private final FindingRepository findingRepository;
    private final ScanRepository scanRepository;

    public FindingService(FindingRepository findingRepository,
                          ScanRepository scanRepository) {
        this.findingRepository = findingRepository;
        this.scanRepository = scanRepository;
    }

    @Transactional
    public FindingResponseDTO createFinding(Long scanId, FindingCreateDTO request) {

        Scan scan = scanRepository.findById(scanId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Scan not found"));

        if (scan.getStatus() != ScanStatus.RUNNING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Findings can only be added to a running scan"
            );
        }

        Finding finding = FindingMapper.toEntity(request);

        scan.addFinding(finding);

        return FindingMapper.toResponseDTO(
                findingRepository.save(finding)
        );
    }
}