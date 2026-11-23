package org.example.depguard.scan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScanRepository extends JpaRepository<Scan, Long> {
    Page<Scan> findByProjectId(Long projectId, Pageable pageable);
    Page<Scan> findByProjectIdAndStatus(
            Long projectId,
            ScanStatus status,
            Pageable pageable
    );

    Optional<Scan> findFirstByProjectIdOrderByCreatedAtDescIdDesc(Long projectId);

    boolean existsByProjectId(Long projectId);

}