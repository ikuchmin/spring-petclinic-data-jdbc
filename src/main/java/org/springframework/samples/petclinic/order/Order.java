package org.springframework.samples.petclinic.order;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Table("order_")
public class Order {

    @Id
    @Column("id")
    private Long id;

    @Column("total_cost")
    private BigDecimal totalCost;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Column("owner_id")
    private AggregateReference<Owner, Integer> owner;

    @Column("pet_id")
    private AggregateReference<Pet, Integer> pet;

    @MappedCollection
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    @MappedCollection
    private Set<OrderDiscount> orderDiscounts = new LinkedHashSet<>();

    @MappedCollection
    private Set<OrderPayment> orderPayments = new LinkedHashSet<>();

    public Set<OrderPayment> getOrderPayments() {
        return orderPayments;
    }

    public void setOrderPayments(Set<OrderPayment> orderPayments) {
        this.orderPayments = orderPayments;
    }

    public Set<OrderDiscount> getOrderDiscounts() {
        return orderDiscounts;
    }

    public void setOrderDiscounts(Set<OrderDiscount> orderDiscounts) {
        this.orderDiscounts = orderDiscounts;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public AggregateReference<Pet, Integer> getPet() {
        return pet;
    }

    public void setPet(AggregateReference<Pet, Integer> pet) {
        this.pet = pet;
    }

    public AggregateReference<Owner, Integer> getOwner() {
        return owner;
    }

    public void setOwner(AggregateReference<Owner, Integer> owner) {
        this.owner = owner;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
