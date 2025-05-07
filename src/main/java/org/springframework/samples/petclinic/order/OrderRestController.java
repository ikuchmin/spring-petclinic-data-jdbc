package org.springframework.samples.petclinic.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.discount.Discount;
import org.springframework.samples.petclinic.discount.DiscountRepository;
import org.springframework.samples.petclinic.order.OrderMapper.OrderExtendedDtoLoadedContext;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.service.Service;
import org.springframework.samples.petclinic.service.ServiceRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/orders")
public class OrderRestController {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final OwnerRepository ownerRepository;

    private final PetRepository petRepository;

    private final DiscountRepository discountRepository;

    private final PaymentRepository paymentRepository;

    private final ServiceRepository serviceRepository;

    private final ObjectMapper objectMapper;

    public OrderRestController(OrderRepository orderRepository,
                               OrderMapper orderMapper,
                               OwnerRepository ownerRepository,
                               PetRepository petRepository,
                               DiscountRepository discountRepository,
                               PaymentRepository paymentRepository,
                               ServiceRepository serviceRepository,
                               ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
        this.discountRepository = discountRepository;
        this.paymentRepository = paymentRepository;
        this.serviceRepository = serviceRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public PagedModel<OrderDto> getAll(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        Page<OrderDto> orderDtoPage = orders.map(orderMapper::toOrderDto);
        return new PagedModel<>(orderDtoPage);
    }

    @GetMapping("/filter")
    public PagedModel<Order> getAllWithFiltering(@ModelAttribute OrderFilter orderFilter, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllWithFiltering(orderFilter.toCriteria(), pageable);
        return new PagedModel<>(orders);
    }

    @GetMapping("/{id}")
    public OrderDto getOne(@PathVariable Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderMapper.toOrderDto(orderOptional.orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    @GetMapping("/by-ids")
    public List<OrderDto> getMany(@RequestParam List<Long> ids) {
        List<Order> orders = orderRepository.findAllById(ids);
        return orders.stream()
            .map(orderMapper::toOrderDto)
            .toList();
    }

    @GetMapping("/{id}/extended")
    public OrderExtendedDto getOneExtended(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        Owner owner = ownerRepository.findById(order.getOwner().getId());
        Pet pet = petRepository.findById(order.getPet().getId());

        List<Long> discountIds = order.getOrderDiscounts().stream()
            .map(OrderDiscount::getDiscount)
            .map(AggregateReference::getId)
            .toList();

        List<Discount> discounts = discountRepository.findAllById(discountIds);

        List<Long> paymentIds = order.getOrderPayments().stream()
            .map(OrderPayment::getPayment)
            .map(AggregateReference::getId)
            .toList();

        List<Payment> payments = paymentRepository.findAllById(paymentIds);

        List<Long> serviceIds = order.getOrderItems().stream()
            .map(OrderItem::getService)
            .map(AggregateReference::getId)
            .toList();

        List<Service> services = serviceRepository.findAllById(serviceIds);

        return orderMapper.toOrderExtendedDto(order,
            new OrderExtendedDtoLoadedContext(owner, pet, discounts, payments, services));
    }

    @GetMapping("/{id}/extended/one-query")
    public OrderExtendedDto getOneExtendedByOneQuery(@PathVariable Long id) {
        return orderRepository.findOrderExtendedById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @PostMapping
    public OrderDto create(@RequestBody OrderCreateDto dto) {
        Order order = orderMapper.toEntity(dto);
        Order resultOrder = orderRepository.save(order);
        return orderMapper.toOrderDto(resultOrder);
    }

    @PostMapping("/bulk")
    public List<OrderCreateDto> createMany(@RequestBody List<OrderCreateDto> dtos) {
        Collection<Order> orders = dtos.stream()
            .map(orderMapper::toEntity)
            .toList();
        List<Order> resultOrders = orderRepository.saveAll(orders);

        return resultOrders.stream()
            .map(orderMapper::toOrderCreateDto)
            .toList();
    }

    @PatchMapping("/{id}")
    public OrderCreateDto patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Order order = orderRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        OrderCreateDto orderCreateDto = orderMapper.toOrderCreateDto(order);
        objectMapper.readerForUpdating(orderCreateDto).readValue(patchNode);
        orderMapper.updateWithNull(orderCreateDto, order);

        Order resultOrder = orderRepository.save(order);
        return orderMapper.toOrderCreateDto(resultOrder);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Order> orders = orderRepository.findAllById(ids);

        for (Order order : orders) {
            OrderCreateDto orderCreateDto = orderMapper.toOrderCreateDto(order);
            objectMapper.readerForUpdating(orderCreateDto).readValue(patchNode);
            orderMapper.updateWithNull(orderCreateDto, order);
        }

        List<Order> resultOrders = orderRepository.saveAll(orders);
        return resultOrders.stream()
            .map(Order::getId)
            .toList();
    }

    @DeleteMapping("/{id}")
    public OrderCreateDto delete(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            orderRepository.delete(order);
        }
        return orderMapper.toOrderCreateDto(order);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        orderRepository.deleteAllById(ids);
    }

    @GetMapping("/report/analyse")
    public List<OrderAnalyseReportDto> findDataForAnalyseReport(@RequestParam Integer minCountOfItems,
                                                                @RequestParam BigDecimal minTotalCost,
                                                                @RequestParam LocalDateTime minCreatedDate,
                                                                @RequestParam LocalDateTime maxCreatedDate) {
        return orderRepository.findDataForAnalyseReport(minCountOfItems, minTotalCost, minCreatedDate, maxCreatedDate);
    }
}

