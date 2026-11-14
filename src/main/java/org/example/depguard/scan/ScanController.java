package org.example.depguard.scan;

import org.example.depguard.dto.ScanResponseDTO;
import org.example.depguard.dto.ScanStatusUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/scans")
public class ScanController {
    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @PostMapping
    public ScanResponseDTO createScan(@PathVariable Long projectId) {
        return scanService.createScan(projectId);
    }

    @GetMapping
    public Page<ScanResponseDTO> getScans(
            @PathVariable Long projectId,
            @RequestParam(required = false) ScanStatus status,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        return scanService.getScansByProjectId(
                projectId, status, pageable);
    }

    @GetMapping("/{scanId}")
    public ScanResponseDTO getScanById(
            @PathVariable Long projectId,
            @PathVariable Long scanId) {
        return scanService.getScanById(projectId, scanId);
    }

    @PatchMapping("/{scanId}/status")
    public ScanResponseDTO updateScanStatus(
            @PathVariable Long projectId,
            @PathVariable Long scanId,
            @RequestBody ScanStatusUpdateDTO request) {

        return scanService.updateScanStatus(projectId, scanId, request);
    }

    @GetMapping("/latest")
    public ScanResponseDTO getLatestScan(
            @PathVariable Long projectId) {

        return scanService.getLatestScan(projectId);
    }
}
