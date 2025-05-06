package org.springframework.samples.petclinic.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.LinkedHashSet;
import java.util.Set;

@Table("service")
public class Service {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    @NotNull
    private String name;

    @Column("status")
    private ServiceStatus status = ServiceStatus.INACTIVE;

    @MappedCollection
    private Set<ServiceSpecialityRef> requiredSpecialities = new LinkedHashSet<>();

    @MappedCollection
    private Set<ServicePriceHistory> servicePriceHistories = new LinkedHashSet<>();

    public Set<ServiceSpecialityRef> getRequiredSpecialities() {
        return requiredSpecialities;
    }

    public void setRequiredSpecialities(Set<ServiceSpecialityRef> requiredSpecialities) {
        this.requiredSpecialities = requiredSpecialities;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public Set<ServicePriceHistory> getServicePriceHistories() {
        return servicePriceHistories;
    }

    public void setServicePriceHistories(Set<ServicePriceHistory> servicePriceHistories) {
        this.servicePriceHistories = servicePriceHistories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
