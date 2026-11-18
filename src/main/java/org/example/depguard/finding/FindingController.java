package org.example.depguard.finding;

import org.example.depguard.dto.FindingCreateDTO;
import org.example.depguard.dto.FindingResponseDTO;
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
    public List<FindingResponseDTO> getFindings(
            @PathVariable Long scanId) {

        return findingService.getFindingsByScanId(scanId);
    }
}