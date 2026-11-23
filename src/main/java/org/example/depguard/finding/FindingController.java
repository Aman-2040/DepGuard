package org.example.depguard.finding;

import org.example.depguard.dto.FindingCreateDTO;
import org.example.depguard.dto.FindingResponseDTO;
import org.example.depguard.dto.FindingSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/scans/{scanId}/findings")
public class FindingController {

    private final FindingService findingService;

    public FindingController(FindingService findingService) {
        this.findingService = findingService;
    }

    @PostMapping
    public ResponseEntity<FindingResponseDTO> createFinding(
            @PathVariable Long scanId,
            @Valid @RequestBody FindingCreateDTO request) {

        FindingResponseDTO response =
                findingService.createFinding(scanId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public Page<FindingResponseDTO> getFindingsByScanId(
            @PathVariable Long scanId,
            @RequestParam(required = false) Severity severity,
            @PageableDefault(sort = "createdAt",
                    direction = Sort.Direction.DESC) Pageable pageable) {

        return findingService.getFindingsByScanId(
                scanId, severity, pageable);
    }

    @GetMapping("/{findingId}")
    public FindingResponseDTO getFindingById(
            @PathVariable Long scanId,
            @PathVariable Long findingId) {

        return findingService.getFindingById(scanId, findingId);
    }

    @GetMapping("/summary")
    public FindingSummaryDTO getFindingSummary(
            @PathVariable Long scanId) {
        return findingService.getFindingSummary(scanId);
    }
}
