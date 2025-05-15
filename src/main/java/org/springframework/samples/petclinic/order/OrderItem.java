package org.springframework.samples.petclinic.order;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.samples.petclinic.service.Service;

import java.math.BigDecimal;

@Table("order_item")
public class OrderItem {

    @Id
    @Column("id")
    private Long id;

    @Column("service_id")
    private AggregateReference<Service, Long> service;

    @Column("quantity")
    private Integer quantity;

    @Column("service_name")
    private String serviceName;

    @Column("price")
    private BigDecimal price;

    @Column("cost")
    private BigDecimal cost;

    @Column("discount_cost")
    private BigDecimal discountCost;

    @Column("discount_reason")
    private String discountReason;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
