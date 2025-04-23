package org.springframework.samples.petclinic.order;

/**
 * DTO for {@link OrderItem}
 */
public record OrderItemServiceDto(Long service, Integer count) {
}
