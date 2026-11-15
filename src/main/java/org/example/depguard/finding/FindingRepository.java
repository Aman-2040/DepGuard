package org.example.depguard.finding;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FindingRepository extends JpaRepository<Finding, Long> {
    List<Finding> findByScanId(Long scanId);
    List<Finding> findByScanIdAndSeverity(Long scanId, Severity severity);
}