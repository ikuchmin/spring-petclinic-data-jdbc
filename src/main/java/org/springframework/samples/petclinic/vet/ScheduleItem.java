package org.springframework.samples.petclinic.vet;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table
public record ScheduleItem(Long id, String itemName, String description) {
}
