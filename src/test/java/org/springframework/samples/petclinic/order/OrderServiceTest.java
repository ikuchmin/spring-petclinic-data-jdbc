package org.springframework.samples.petclinic.order;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles(value = "postgres")
class OrderServiceTest {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceTest.class);
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void checkThatOrderCreationFillAuditingAttributes() {
        OrderDto order = orderService.createOrder();

        assertNotNull(order.createdDate());
        assertNotNull(order.lastModifiedDate());

        // cleanup
        orderRepository.deleteById(order.id());
    }

    @Test
    @Sql(scripts = "/org/springframework/samples/petclinic/order/order.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/org/springframework/samples/petclinic/order/order_delete.sql", executionPhase = AFTER_TEST_METHOD)
    void checkThatAddingServiceRecalculateCostFields() {
        long orderId = 1L;
        long serviceId = 3L;

        OrderItemServiceDto orderItemServiceDto = new OrderItemServiceDto(serviceId, 1);
        var order = orderService.addServiceItem(orderId, orderItemServiceDto);

        var orderItem = order.getOrderItems().getLast();

        assertEquals(3, order.getOrderItems().size());
        assertEquals(600.00, order.getTotalCost().doubleValue());

        assertEquals(3, orderItem.getService().getId());
        assertEquals(1, orderItem.getCount());

        assertEquals("Vaccination", orderItem.getServiceName());
        assertEquals(25.00, orderItem.getPrice().doubleValue());
        assertEquals(25.00, orderItem.getCost().doubleValue());
    }

    @Test
    @Sql(scripts = "/org/springframework/samples/petclinic/order/discount.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/org/springframework/samples/petclinic/order/discount_delete.sql", executionPhase = AFTER_TEST_METHOD)
    void checkThatApplyingDiscountHasImpactOnItemCost() {
        long orderId = 1L;

        log.info("Ready to apply discount");
        orderService.applyDiscount(orderId, "DISCOUNT10");
        log.info("Discount applied");

        Order order = orderRepository.findById(orderId).orElseThrow();
        OrderItem grooming = order.getOrderItems().stream()
            .filter(oi -> oi.getService().getId() == 1L)
            .findAny().orElseThrow();

        OrderItem petCheckup = order.getOrderItems().stream()
            .filter(oi -> oi.getService().getId() == 2L)
            .findAny().orElseThrow();

        assertEquals(575.00 - 57.50, order.getTotalCost().doubleValue());
        assertEquals(75.00 - 7.5, grooming.getCost().doubleValue());
        assertEquals(7.5, grooming.getDiscountCost().doubleValue());
        assertEquals(500.00 - 50.00, petCheckup.getCost().doubleValue());
        assertEquals(50.00, petCheckup.getDiscountCost().doubleValue());
    }
}
