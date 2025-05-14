package org.springframework.samples.petclinic.service;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Table("service")
public class Service {

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("name")
    private String name;

    @MappedCollection
    private Set<ServicePriceHistory> servicePriceHistories = new LinkedHashSet<>();

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
