package org.springframework.samples.petclinic.order;

/**
 * DTO for {@link Order}
 */
public record OrderCreateDto(Integer owner, Integer pet) {
}
