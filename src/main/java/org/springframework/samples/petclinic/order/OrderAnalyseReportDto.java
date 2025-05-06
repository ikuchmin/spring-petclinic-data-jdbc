package org.springframework.samples.petclinic.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link Order}
 */
public record OrderAnalyseReportDto(Long id, BigDecimal totalCost,
                                    LocalDateTime createdDate,
                                    LocalDateTime lastModifiedDate,
                                    Integer itemsCount) {
}
