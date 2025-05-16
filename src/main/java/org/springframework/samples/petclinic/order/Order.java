package org.springframework.samples.petclinic.order;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.samples.petclinic.owner.Owner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Table("order_")
public class Order {

    @Id
    @Column("id")
    private Long id;

    @Column("number")
    private String number;

    @Column("status")
    private OrderStatus status = OrderStatus.PENDING;

    @Column("owner_id")
    private AggregateReference<Owner, Integer> owner;

    @MappedCollection
    private Set<OrderPaymentRef> orderPaymentRefs = new LinkedHashSet<>();

    @MappedCollection
    private Set<OrderDiscountRef> orderDiscountRefs = new LinkedHashSet<>();

    @MappedCollection
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column("total_cost")
    private BigDecimal totalCost;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public Set<OrderDiscountRef> getOrderDiscountRefs() {
        return orderDiscountRefs;
    }

    public void setOrderDiscountRefs(Set<OrderDiscountRef> orderDiscountRefs) {
        this.orderDiscountRefs = orderDiscountRefs;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Set<OrderPaymentRef> getOrderPaymentRefs() {
        return orderPaymentRefs;
    }

    public void setOrderPaymentRefs(Set<OrderPaymentRef> orderPaymentRefs) {
        this.orderPaymentRefs = orderPaymentRefs;
    }


    public AggregateReference<Owner, Integer> getOwner() {
        return owner;
    }

    public void setOwner(AggregateReference<Owner, Integer> owner) {
        this.owner = owner;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
