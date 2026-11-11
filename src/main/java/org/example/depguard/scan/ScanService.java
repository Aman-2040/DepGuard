
package org.example.depguard.scan;

import org.example.depguard.dto.ScanResponseDTO;
import org.example.depguard.mapper.ScanMapper;
import org.example.depguard.project.Project;
import org.example.depguard.project.ProjectRepository;
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

        Scan scan = new Scan("PENDING", LocalDateTime.now());

        project.addScan(scan);

        Scan savedScan = scanRepository.save(scan);

        return ScanMapper.toDTO(savedScan);
    }

    public List<ScanResponseDTO> getScansByProjectId(Long projectId) {

        projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"
                ));

        List<Scan> scans = scanRepository.findByProjectId(projectId);

        return scans.stream()
                .map(ScanMapper::toDTO)
                .toList();
    }
}
