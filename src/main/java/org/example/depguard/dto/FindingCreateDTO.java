package org.example.depguard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.depguard.finding.Severity;

public class FindingCreateDTO {

    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 2000)
    private String description;

    @NotNull
    private Severity severity;

    @NotBlank
    @Size(max = 255)
    private String dependencyName;

    @NotBlank
    @Size(max = 50)
    private String installedVersion;

    @Size(max = 50)
    private String fixedVersion;

    @Size(max = 100)
    private String vulnerabilityId;

    public FindingCreateDTO(){}

    public FindingCreateDTO(String title, String description, Severity severity, String dependencyName, String installedVersion, String fixedVersion, String vulnerabilityId) {

        this.title = title;
        this.description = description;
        this.severity = severity;
        this.dependencyName = dependencyName;
        this.installedVersion = installedVersion;
        this.fixedVersion = fixedVersion;
        this.vulnerabilityId = vulnerabilityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getDependencyName() {
        return dependencyName;
    }

    public void setDependencyName(String dependencyName) {
        this.dependencyName = dependencyName;
    }

    public String getInstalledVersion() {
        return installedVersion;
    }

    public void setInstalledVersion(String installedVersion) {
        this.installedVersion = installedVersion;
    }

    public String getFixedVersion() {
        return fixedVersion;
    }

    public void setFixedVersion(String fixedVersion) {
        this.fixedVersion = fixedVersion;
    }

    public String getVulnerabilityId() {
        return vulnerabilityId;
    }

    public void setVulnerabilityId(String vulnerabilityId) {
        this.vulnerabilityId = vulnerabilityId;
    }
}
