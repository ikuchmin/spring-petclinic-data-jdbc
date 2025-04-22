package org.springframework.samples.petclinic.vet;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.samples.petclinic.owner.Pet;

import java.util.ArrayList;
import java.util.List;

@Table("schedule")
public class Schedule {

    @Id
    @Column("id")
    private Long id;

    @Column("vet_id")
    private AggregateReference<Vet, Long> vet;


    public AggregateReference<Vet, Long> getVet() {
        return vet;
    }

    public void setVet(AggregateReference<Vet, Long> vet) {
        this.vet = vet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
