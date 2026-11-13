
package org.example.depguard.scan;

import org.example.depguard.dto.ScanResponseDTO;
import org.example.depguard.dto.ScanStatusUpdateDTO;
import org.example.depguard.mapper.ScanMapper;
import org.example.depguard.project.Project;
import org.example.depguard.project.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScanService {

    private final ScanRepository scanRepository;
    private final ProjectRepository projectRepository;

    public ScanService(ScanRepository scanRepository, ProjectRepository projectRepository) {
        this.scanRepository = scanRepository;
        this.projectRepository = projectRepository;
    }

    public ScanResponseDTO createScan(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        Scan scan = new Scan(ScanStatus.PENDING);

        project.addScan(scan);

        Scan savedScan = scanRepository.save(scan);

        return ScanMapper.toDTO(savedScan);
    }

    public Page<ScanResponseDTO> getScansByProjectId(
            Long projectId, ScanStatus status, Pageable pageable) {

        projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"
                ));

        Page<Scan> scans;

        if (status != null) {
            scans = scanRepository.findByProjectIdAndStatus(
                    projectId, status, pageable);
        } else {
            scans = scanRepository.findByProjectId(
                    projectId, pageable);
        }

        return scans.map(ScanMapper::toDTO);
    }
    public ScanResponseDTO getScanById(Long projectId, Long scanId) {

        projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"
                ));

        Scan scan = scanRepository.findById(scanId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Scan not found"
                ));

        if (!scan.getProject().getId().equals(projectId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Scan not found in this project"
            );
        }

        return ScanMapper.toDTO(scan);
    }

    public ScanResponseDTO updateScanStatus(Long projectId, Long scanId, ScanStatusUpdateDTO request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));
        Scan scan = scanRepository.findById(scanId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Scan not found"
                ));
        if (!scan.getProject().getId().equals(project.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Scan not found in this project"
            );
        }
        if (request == null || request.getStatus() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Status is required"
            );
        }
        ScanStatus current = scan.getStatus();
        ScanStatus requested = request.getStatus();
        boolean allowed =
                (current == ScanStatus.PENDING &&
                        (requested == ScanStatus.RUNNING ||
                                requested == ScanStatus.CANCELLED))
                        ||
                        (current == ScanStatus.RUNNING &&
                                (requested == ScanStatus.COMPLETED ||
                                        requested == ScanStatus.FAILED));
        if (!allowed) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid scan status transition"
            );
        }
        scan.setStatus(requested);

        if (requested == ScanStatus.RUNNING) {
            scan.setStartedAt(LocalDateTime.now());
        }

        if (requested == ScanStatus.COMPLETED ||
                requested == ScanStatus.FAILED) {
            scan.setCompletedAt(LocalDateTime.now());
        }
        Scan savedScan = scanRepository.save(scan);
        return ScanMapper.toDTO(savedScan);
    }
}
