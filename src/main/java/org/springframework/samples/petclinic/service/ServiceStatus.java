package org.springframework.samples.petclinic.service;

public enum ServiceStatus {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String status;

    ServiceStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
