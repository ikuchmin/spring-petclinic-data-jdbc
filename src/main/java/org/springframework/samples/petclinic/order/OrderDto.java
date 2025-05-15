package org.springframework.samples.petclinic.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link Order}
 */
public record OrderDto(Long id, String number, Integer owner, Set<Long> orderPaymentRefPayments,
                       List<OrderItemDto> orderItems, BigDecimal totalCost, LocalDateTime createdDate,
                       LocalDateTime lastModifiedDate) {
    /**
     * DTO for {@link OrderItem}
     */
    public record OrderItemDto(Long id, Long service, Integer quantity, String serviceName, BigDecimal price,
                               BigDecimal cost) {
    }
}
