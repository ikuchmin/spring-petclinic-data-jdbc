package org.springframework.samples.petclinic.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link Order}
 */
public record OrderGetAllDto(Long id, String number, Integer owner, BigDecimal totalCost, LocalDateTime createdDate,
                             LocalDateTime lastModifiedDate) {
}
