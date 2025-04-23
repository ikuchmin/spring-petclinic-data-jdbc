package org.springframework.samples.petclinic.order;

import org.junit.jupiter.api.Test;
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
        Order preOrder = orderRepository.findById(orderId).orElseThrow();

        long serviceId = 2L;

        OrderItemServiceDto orderItemServiceDto = new OrderItemServiceDto(serviceId, 1);
        OrderDto orderDto = orderService.addServiceItem(orderId, orderItemServiceDto);

        Order order = orderRepository.findById(orderId).orElseThrow();

        Set<OrderItem> diffItems = new HashSet<>(order.getOrderItems());
        diffItems.removeAll(preOrder.getOrderItems());

        assert diffItems.size() == 1;

        var orderItem = diffItems.stream().findAny().orElseThrow();

        assertEquals(2, order.getOrderItems().size());
        assertEquals(575.00, order.getTotalCost().doubleValue());

        assertEquals(2, orderItem.getService().getId());
        assertEquals(1, orderItem.getCount());

        assertEquals("Pet Checkup", orderItem.getServiceName());
        assertEquals(500.00, orderItem.getPrice().doubleValue());
        assertEquals(500.00, orderItem.getCost().doubleValue());
    }
}
