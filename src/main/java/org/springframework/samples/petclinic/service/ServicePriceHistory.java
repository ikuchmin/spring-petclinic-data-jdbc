package org.springframework.samples.petclinic.service;

import jakarta.validation.constraints.Digits;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("service_price_history")
public class ServicePriceHistory {

    @Id
    @Column("id")
    private Long id;

    @Column("price")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @Column("effective_from")
    private LocalDateTime effectiveFrom;

    public LocalDateTime getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDateTime effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
