package org.springframework.samples.petclinic.passport;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table("person")
public class Person {

    @Id
    @Column("id")
    private Long id;

    @MappedCollection
    private List<PassportRef> passportRefs = new ArrayList<>();

    public List<PassportRef> getPassportRefs() {
        return passportRefs;
    }

    public void setPassportRefs(List<PassportRef> passportRefs) {
        this.passportRefs = passportRefs;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
