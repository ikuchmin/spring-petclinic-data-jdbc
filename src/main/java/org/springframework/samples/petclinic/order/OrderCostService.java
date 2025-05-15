package org.springframework.samples.petclinic.order;

import org.springframework.stereotype.Service;

@Service
public class OrderCostService {

    public Order calculateCostOrder(Order order) {
        return order;
    }
}
