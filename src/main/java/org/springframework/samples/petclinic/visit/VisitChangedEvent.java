package org.springframework.samples.petclinic.visit;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

/**
 * DTO for {@link Visit}
 */
public record VisitChangedEvent(Long id, LocalDate date, @NotEmpty String description, Integer petId, Integer pet,
                                Integer owner) {
}
