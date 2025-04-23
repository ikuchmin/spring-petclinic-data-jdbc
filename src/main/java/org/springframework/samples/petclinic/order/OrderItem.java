package org.springframework.samples.petclinic.order;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.samples.petclinic.service.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Table("order_item")
public class OrderItem {

    @Id
    @Column("id")
    private Long id;

    @Column("service_id")
    private AggregateReference<Service, Long> service;

    @Column("service_name")
    private String serviceName;

    @Column("price")
    private BigDecimal price;

    @Column("count")
    private Integer count;

    @Column("discount_cost")
    private BigDecimal discountCost;

    @Column("discount_reason")
    private String discountReason;

    @Column("cost")
    private BigDecimal cost;

    public String getDiscountReason() {
        return discountReason;
    }

    public void setDiscountReason(String discountReason) {
        this.discountReason = discountReason;
    }

    public BigDecimal getDiscountCost() {
        return discountCost;
    }

    public void setDiscountCost(BigDecimal discountCost) {
        this.discountCost = discountCost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public AggregateReference<Service, Long> getService() {
        return service;
    }

    public void setService(AggregateReference<Service, Long> service) {
        this.service = service;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
