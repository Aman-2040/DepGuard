package org.example.depguard.scan;

import org.example.depguard.dto.ScanResponseDTO;
import org.example.depguard.dto.ScanStatusUpdateDTO;
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
    public List<ScanResponseDTO> getScansByProjectId(@PathVariable Long projectId) {
        return scanService.getScansByProjectId(projectId);
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
}
