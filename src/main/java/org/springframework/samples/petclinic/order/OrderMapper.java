package org.springframework.samples.petclinic.order;

import org.mapstruct.*;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.samples.petclinic.discount.Discount;
import org.springframework.samples.petclinic.payment.Payment;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderGetAllDto toOrderDto(Order order);

    default <T, R> R mapFromAggregateReference(AggregateReference<T, R> aggregateReference) {
        if (aggregateReference == null) {
            return null;
        }
        return aggregateReference.getId();
    }

    @Mapping(target = "orderPaymentRefPayments", expression = "java(orderPaymentRefsToOrderPaymentRefPayments(order.getOrderPaymentRefs()))")
    OrderDto toOrderGetOneDto(Order order);

    default Set<Long> orderPaymentRefsToOrderPaymentRefPayments(Set<OrderPaymentRef> orderPaymentRefs) {
        return orderPaymentRefs.stream()
            .map(OrderPaymentRef::getPayment)
            .map(AggregateReference::getId)
            .collect(Collectors.toSet());
    }

    OrderExtendedDto toOrderExtendedDto(Order order, @Context OrderGetOneExtendedDtoMappingContext orderContext);

    OrderExtendedDto.PaymentDto toOrderGetOneExtendedDto_PaymentDto(OrderPaymentRef orderPaymentRef, @Context OrderGetOneExtendedDtoMappingContext orderContext);

    record OrderGetOneExtendedDtoMappingContext(List<Payment> payments, List<Discount> discounts) {
    }
}
