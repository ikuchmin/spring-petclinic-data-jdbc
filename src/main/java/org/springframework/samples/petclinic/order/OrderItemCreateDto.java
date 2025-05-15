package org.springframework.samples.petclinic.order;

/**
 * DTO for {@link OrderItem}
 */
public record OrderItemCreateDto(Long service, Integer quantity) {
}
