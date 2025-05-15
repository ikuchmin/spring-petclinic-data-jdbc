package org.springframework.samples.petclinic.order;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.samples.petclinic.discount.Discount;

@Table("order_discount_ref")
public class OrderDiscountRef {

    @Id
    @Column("id")
    private Long id;

    @Column("discount_id")
    private AggregateReference<Discount, Long> discount;

    public AggregateReference<Discount, Long> getDiscount() {
        return discount;
    }

    public void setDiscount(AggregateReference<Discount, Long> discount) {
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
