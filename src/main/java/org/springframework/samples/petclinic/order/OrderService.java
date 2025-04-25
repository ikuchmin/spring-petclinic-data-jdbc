package org.springframework.samples.petclinic.order;

import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.samples.petclinic.discount.Discount;
import org.springframework.samples.petclinic.discount.DiscountRepository;
import org.springframework.samples.petclinic.service.ServiceDto;
import org.springframework.samples.petclinic.service.ServiceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final ServiceService serviceService;

    private final DiscountRepository discountRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderMapper orderMapper,
                        ServiceService serviceService,
                        DiscountRepository discountRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.serviceService = serviceService;
        this.discountRepository = discountRepository;
    }

    public OrderDto createOrder() {
        Order order = new Order();
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderDto(savedOrder);
    }

    public Order addServiceItem(Long orderId, OrderItemServiceDto orderItem) {

        Order order = orderRepository.findById(orderId).orElseThrow();

        OrderItem item = orderMapper.toOrderItemEntity(orderItem);
        order.getOrderItems().add(item);

        recalculateOrderItemCost(item);
        recalculateOrderCost(order);

        return orderRepository.save(order);
    }

    // discount can be applied for each position or for appropriated
    public Order applyDiscount(Long orderId, String discountCode) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        Discount discount = discountRepository.findByCode(discountCode).orElseThrow();

        OrderDiscount orderDiscount = new OrderDiscount();
        orderDiscount.setDiscount(AggregateReference.to(discount.getId()));

        order.getOrderDiscounts().add(orderDiscount);

        recalculateOrderCost(order);

        return orderRepository.save(order);
    }

    private void recalculateOrderCost(Order order) {

        // recalculate order item cost
        order.getOrderItems().forEach(oi ->
            oi.setCost(oi.getPrice().multiply(BigDecimal.valueOf(oi.getCount()))));

        // apply discount for each order item
        if (! order.getOrderDiscounts().isEmpty()) {
            List<Long> discountIds = order.getOrderDiscounts().stream()
                .map(od -> od.getDiscount().getId())
                .toList();

            List<Discount> discounts = discountRepository.findAllById(discountIds).stream()
                .sorted(Comparator.comparing(d -> d.getPercent().doubleValue()))
                .toList();

            String discountReason = discounts.stream()
                .map(Discount::getCode).collect(joining(", "));

            for (OrderItem orderItem : order.getOrderItems()) {
                discounts.forEach(d -> {
                    var discount = d.getPercent().multiply(orderItem.getCost());
                    orderItem.setDiscountCost(orderItem.getDiscountCost().add(discount));
                    orderItem.setCost(orderItem.getCost().subtract(discount));
                });

                orderItem.setDiscountReason(discountReason);
            }

        }

        order.setTotalCost(order.getOrderItems().stream()
            .map(OrderItem::getCost)
            .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private void recalculateOrderItemCost(OrderItem item) {
        ServiceDto service = serviceService.retrieveService(item.getService().getId());
        item.setServiceName(service.name());
        item.setPrice(service.price());
        item.setCost(item.getPrice().multiply(BigDecimal.valueOf(item.getCount())));
    }
}
