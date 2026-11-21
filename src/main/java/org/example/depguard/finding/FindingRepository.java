package org.example.depguard.finding;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;

public interface FindingRepository extends JpaRepository<Finding, Long> {
    Page<Finding> findByScanId(Long scanId, Pageable pageable);

    Page<Finding> findByScanIdAndSeverity(
            Long scanId, Severity severity, Pageable pageable);
    Optional<Finding> findByScanIdAndId(Long scanId, Long id);
}