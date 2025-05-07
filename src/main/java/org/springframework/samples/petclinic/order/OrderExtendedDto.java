package org.springframework.samples.petclinic.order;

import org.springframework.samples.petclinic.discount.Discount;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.service.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link Order}
 */
public record OrderExtendedDto(Long id, BigDecimal totalCost, LocalDateTime createdDate, OwnerDto owner, PetDto pet,
                               List<OrderItemDto> orderItems, Set<DiscountDto> orderDiscounts,
                               Set<PaymentDto> orderPayments) {
    /**
     * DTO for {@link OrderItem}
     */
    public record OrderItemDto(Long id, ServiceDto service, String serviceName, BigDecimal price, Integer count,
                               BigDecimal discountCost, String discountReason, BigDecimal cost) {
    }

    /**
     * DTO for {@link Discount}
     */
    public record DiscountDto(Long id, String code, String description, BigDecimal percent) {
    }

    /**
     * DTO for {@link Payment}
     */
    public record PaymentDto(Long id, String method, LocalDateTime paidAt, BigDecimal amount) {
    }

    /**
     * DTO for {@link Service}
     */
    public record ServiceDto(Long id, String name) {
    }

    /**
     * DTO for {@link Owner}
     */
    public record OwnerDto(Integer id, String firstName, String lastName, String telephone) {

    }

    /**
     * DTO for {@link Pet}
     */
    public record PetDto(Integer id, String name, LocalDate birthDate, String type) {
    }
}
