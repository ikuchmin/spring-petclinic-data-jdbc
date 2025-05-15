package org.springframework.samples.petclinic.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link Order}
 */
public record OrderExtendedDto(Long id, String number, Integer owner, Set<PaymentDto> payments, Set<DiscountDto> discounts,
                               List<OrderItemDto> orderItems, BigDecimal totalCost, LocalDateTime createdDate,
                               LocalDateTime lastModifiedDate) {
    /**
     * DTO for {@link org.springframework.samples.petclinic.payment.Payment}
     */
    public record PaymentDto(Long id, String method, LocalDateTime paidAt, BigDecimal amount, String message) {
    }

    /**
     * DTO for {@link OrderItem}
     */
    public record OrderItemDto(Long id, Long service, Integer quantity, String serviceName, BigDecimal price,
                               BigDecimal cost) {
    }

    /**
     * DTO for {@link org.springframework.samples.petclinic.discount.Discount}
     */
    public record DiscountDto(Long id, String code, String description, BigDecimal percent) {
    }
}
