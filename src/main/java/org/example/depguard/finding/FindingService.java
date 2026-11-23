package org.example.depguard.finding;

import org.example.depguard.dto.FindingCreateDTO;
import org.example.depguard.dto.FindingResponseDTO;
import org.example.depguard.dto.FindingSummaryDTO;
import org.example.depguard.mapper.FindingMapper;
import org.example.depguard.scan.Scan;
import org.example.depguard.scan.ScanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.example.depguard.scan.ScanRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @Transactional(readOnly = true)
    public Page<FindingResponseDTO> getFindingsByScanId(
            Long scanId, Severity severity, Pageable pageable) {

        scanRepository.findById(scanId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Scan not found"));

        Page<Finding> findings;

        if (severity == null) {
            findings = findingRepository.findByScanId(scanId, pageable);
        } else {
            findings = findingRepository.findByScanIdAndSeverity(
                    scanId, severity, pageable);
        }

        return findings.map(FindingMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public FindingResponseDTO getFindingById(Long scanId, Long findingId) {

        scanRepository.findById(scanId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Scan not found"));

        Finding finding = findingRepository
                .findByScanIdAndId(scanId, findingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Finding not found"));

        return FindingMapper.toResponseDTO(finding);
    }

    @Transactional(readOnly = true)
    public FindingSummaryDTO getFindingSummary(Long scanId) {

        scanRepository.findById(scanId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Scan not found"));



        return FindingMapper.toSummaryDTO(
                findingRepository.countFindingsBySeverity(scanId)
        );
    }
}