package org.springframework.samples.petclinic.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.discount.Discount;
import org.springframework.samples.petclinic.discount.DiscountRepository;
import org.springframework.samples.petclinic.payment.Payment;
import org.springframework.samples.petclinic.payment.PaymentRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/orders")
public class OrderRestController {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final PaymentRepository paymentRepository;

    private final OrderItemMapper orderItemMapper;

    private final DiscountRepository discountRepository;

    private final OrderCostService orderCostService;


    public OrderRestController(OrderRepository orderRepository,
                               OrderMapper orderMapper,
                               PaymentRepository paymentRepository,
                               OrderItemMapper orderItemMapper,
                               DiscountRepository discountRepository,
                               OrderCostService orderCostService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.paymentRepository = paymentRepository;
        this.orderItemMapper = orderItemMapper;
        this.discountRepository = discountRepository;
        this.orderCostService = orderCostService;
    }

    @GetMapping
    public PagedModel<OrderGetAllDto> getAll(@ModelAttribute OrderGetAllFilter orderFilter, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllWithFiltering(orderFilter.toCriteria(), pageable);
        Page<OrderGetAllDto> orderDtoPage = orders.map(orderMapper::toOrderDto);
        return new PagedModel<>(orderDtoPage);
    }

    @GetMapping("/{id}")
    public OrderDto getOne(@PathVariable Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderMapper.toOrderGetOneDto(orderOptional.orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    @GetMapping("/{id}/extended")
    public OrderExtendedDto getOneExtended(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        List<Long> paymentIds = order.getOrderPaymentRefs().stream()
            .map(OrderPaymentRef::getPayment)
            .map(AggregateReference::getId)
            .toList();

        List<Payment> payments = paymentRepository.findAllById(paymentIds);

        List<Long> discountIds = order.getOrderDiscountRefs().stream()
            .map(OrderDiscountRef::getDiscount)
            .map(AggregateReference::getId)
            .toList();

        List<Discount> discounts = discountRepository.findAllById(discountIds);

        return orderMapper.toOrderExtendedDto(order,
            new OrderMapper.OrderGetOneExtendedDtoMappingContext(payments, discounts));
    }

    @PostMapping("/{id}/items")
    public OrderExtendedDto createOrderItem(@PathVariable Long id, OrderItemCreateDto createDto) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order with id `%s` not found".formatted(id)));

        OrderItem orderItem = orderItemMapper.toEntity(createDto);

        // todo: fill deduplicate field

        order.getOrderItems().add(orderItem);

        Order savedOrder = orderRepository.save(
            orderCostService.calculateCostOrder(order));

        List<Long> paymentIds = order.getOrderPaymentRefs().stream()
            .map(OrderPaymentRef::getPayment)
            .map(AggregateReference::getId)
            .toList();

        List<Payment> payments = paymentRepository.findAllById(paymentIds);

        List<Long> discountIds = order.getOrderDiscountRefs().stream()
            .map(OrderDiscountRef::getDiscount)
            .map(AggregateReference::getId)
            .toList();

        List<Discount> discounts = discountRepository.findAllById(discountIds);

        return orderMapper.toOrderExtendedDto(savedOrder,
            new OrderMapper.OrderGetOneExtendedDtoMappingContext(payments, discounts));
    }

    @PostMapping("/{id}/applyDiscount")
    public OrderExtendedDto applyDiscount(@PathVariable Long id, DiscountCodeDto discountCodeDto) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order with id `%s` not found".formatted(id)));
        Discount discount = discountRepository.findByCode(discountCodeDto.code());

        // connect discount with order
        OrderDiscountRef orderDiscountRef = new OrderDiscountRef();
        orderDiscountRef.setDiscount(AggregateReference.to(discount.getId()));
        order.getOrderDiscountRefs().add(orderDiscountRef);

        Order savedOrder = orderRepository.save(
            orderCostService.calculateCostOrder(order));

        List<Long> paymentIds = order.getOrderPaymentRefs().stream()
            .map(OrderPaymentRef::getPayment)
            .map(AggregateReference::getId)
            .toList();

        List<Payment> payments = paymentRepository.findAllById(paymentIds);

        List<Long> discountIds = order.getOrderDiscountRefs().stream()
            .map(OrderDiscountRef::getDiscount)
            .map(AggregateReference::getId)
            .toList();

        List<Discount> discounts = discountRepository.findAllById(discountIds);

        return orderMapper.toOrderExtendedDto(savedOrder,
            new OrderMapper.OrderGetOneExtendedDtoMappingContext(payments, discounts));
    }
}

