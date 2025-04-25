package org.springframework.samples.petclinic.order;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.samples.petclinic.discount.Discount;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.service.Service;

import java.util.Collection;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderDto toOrderDto(Order order);

    OrderItem toOrderItemEntity(OrderItemServiceDto orderItemServiceDto);

    default <T, R> AggregateReference<T, R> mapToAggregateReference(R id) {
        if (id == null) {
            return null;
        }
        return AggregateReference.to(id);
    }

    default <T, R> R mapFromAggregateReference(AggregateReference<T, R> aggregateReference) {
        if (aggregateReference == null) {
            return null;
        }
        return aggregateReference.getId();
    }

//    Order toEntity(OrderExtendedDto orderExtendedDto);

    OrderExtendedDto toOrderExtendedDto(Order order, @Context OrderExtendedDtoLoadedContext context);

    default OrderExtendedDto.OwnerDto toOrderExtendedDto_OwnerDto(AggregateReference<Owner, Integer> ownerId,
                                                                  @Context OrderExtendedDtoLoadedContext context) {
        Owner owner = context.owner;
        if (! ownerId.getId().equals(owner.getId())) {
            throw new IllegalStateException("Owner ID mismatch: expected " + ownerId.getId() + " but got " + owner.getId());
        }

        return toOrderExtendedDto_OwnerDto(owner);
    }

    OrderExtendedDto.OwnerDto toOrderExtendedDto_OwnerDto(Owner owner);

    default OrderExtendedDto.PetDto toOrderExtendedDto_PetDto(AggregateReference<Pet, Integer> petId,
                                                              @Context OrderExtendedDtoLoadedContext context) {
        Pet pet = context.pet;
        if (! petId.getId().equals(pet.getId())) {
            throw new IllegalStateException("Pet ID mismatch: expected " + petId.getId() + " but got " + pet.getId());
        }

        return toOrderExtendedDto_PetDto(pet);
    }

    OrderExtendedDto.PetDto toOrderExtendedDto_PetDto(Pet pet);

    default OrderExtendedDto.DiscountDto toOrderExtendedDto_DiscountDto(OrderDiscount orderDiscount,
                                                                        @Context OrderExtendedDtoLoadedContext context) {
        Discount discount = context.discounts().stream()
            .filter(d -> d.getId().equals(orderDiscount.getDiscount().getId()))
            .findAny().orElseThrow();

        return toOrderExtendedDto_DiscountDto(discount);
    }

    OrderExtendedDto.DiscountDto toOrderExtendedDto_DiscountDto(Discount discount);

    default OrderExtendedDto.PaymentDto toOrderExtendedDto_PaymentDto(OrderPayment orderPayment,
                                                                      @Context OrderExtendedDtoLoadedContext context) {
        Payment payment = context.payments().stream()
            .filter(p -> p.getId().equals(orderPayment.getPayment().getId()))
            .findAny().orElseThrow();

        return toOrderExtendedDto_PaymentDto(payment);
    }

    OrderExtendedDto.PaymentDto toOrderExtendedDto_PaymentDto(Payment payment);

    default OrderExtendedDto.ServiceDto toOrderExtendedDto_ServiceDto(AggregateReference<Service, Long> serviceId,
                                                                      @Context OrderExtendedDtoLoadedContext context) {
        Service service = context.services().stream()
            .filter(s -> s.getId().equals(serviceId.getId()))
            .findAny().orElseThrow();

        return toOrderExtendedDto_ServiceDto(service);
    }

    OrderExtendedDto.ServiceDto toOrderExtendedDto_ServiceDto(Service service);

    record OrderExtendedDtoLoadedContext(Owner owner,
                                         Pet pet,
                                         Collection<Discount> discounts,
                                         Collection<Payment> payments,
                                         Collection<Service> services) {
    }
}
