package org.springframework.samples.petclinic.order;

public enum OrderStatus {
    PENDING("Pending"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
