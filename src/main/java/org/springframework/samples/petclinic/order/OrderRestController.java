package org.springframework.samples.petclinic.order;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

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

    public OrderRestController(OrderRepository orderRepository,
                               OrderMapper orderMapper,
                               OwnerRepository ownerRepository,
                               PetRepository petRepository,
                               DiscountRepository discountRepository,
                               PaymentRepository paymentRepository,
                               ServiceRepository serviceRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
        this.discountRepository = discountRepository;
        this.paymentRepository = paymentRepository;
        this.serviceRepository = serviceRepository;
    }

    @GetMapping("/{id}")
    public OrderExtendedDto getOne(@PathVariable Long id) {
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

    @GetMapping
    public PagedModel<OrderDto> getAll(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        Page<OrderDto> orderDtoPage = orders.map(orderMapper::toOrderDto);
        return new PagedModel<>(orderDtoPage);
    }
}

