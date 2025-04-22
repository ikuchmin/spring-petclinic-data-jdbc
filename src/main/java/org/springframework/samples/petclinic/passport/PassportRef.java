package org.springframework.samples.petclinic.passport;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("passport_ref")
public class PassportRef {

    @Id
    @Column("id")
    private Long id;

    @Column("passport_id")
    private AggregateReference<Passport, Long> passport;

    public AggregateReference<Passport, Long> getPassport() {
        return passport;
    }

    public void setPassport(AggregateReference<Passport, Long> passport) {
        this.passport = passport;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
