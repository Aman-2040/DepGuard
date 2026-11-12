package org.example.depguard.dto;

import org.example.depguard.scan.ScanStatus;

public class ScanStatusUpdateDTO {
    private ScanStatus status;

    public ScanStatus getStatus() {
        return status;
    }

    public void setStatus(ScanStatus status) {
        this.status = status;
    }
}
