package org.springframework.samples.petclinic.service;

import java.math.BigDecimal;

/**
 * DTO for {@link Service}
 */
public record ServiceDto(Long id, String name, BigDecimal price) {
}
