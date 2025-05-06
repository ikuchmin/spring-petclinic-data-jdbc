package org.springframework.samples.petclinic.service;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.samples.petclinic.vet.Specialty;

@Table("service_speciality_ref")
public class ServiceSpecialityRef {

    @Id
    @Column("id")
    private Long id;

    @Column("specialty_id")
    private AggregateReference<Specialty, Long> specialty;

    public AggregateReference<Specialty, Long> getSpecialty() {
        return specialty;
    }

    public void setSpecialty(AggregateReference<Specialty, Long> specialty) {
        this.specialty = specialty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
