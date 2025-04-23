package org.springframework.samples.petclinic.order;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("order_payment")
public class OrderPayment {

    @Id
    @Column("id")
    private Long id;

    @Column("payment_id")
    private AggregateReference<Payment, Long> payment;

    public AggregateReference<Payment, Long> getPayment() {
        return payment;
    }

    public void setPayment(AggregateReference<Payment, Long> payment) {
        this.payment = payment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
