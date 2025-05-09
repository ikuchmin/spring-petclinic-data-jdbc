/*
 * Copyright 2012-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.visit;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;

/**
 * Simple JavaBean domain object representing a visit.
 *
 * @author Ken Krebs
 * @author Dave Syer
 * @author Maciej Walkowiak
 */
@Table("visit")
public class Visit {

	@Id
	private Long id;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@NotEmpty
	private String description;

	private Integer petId;

    @Column("pet_id")
    private AggregateReference<Pet, Integer> pet;

    @Column("owner_id")
    private AggregateReference<Owner, Integer> owner;

    public AggregateReference<Owner, Integer> getOwner() {
        return owner;
    }

    public void setOwner(AggregateReference<Owner, Integer> owner) {
        this.owner = owner;
    }

    public AggregateReference<Pet, Integer> getPet() {
        return pet;
    }

    public void setPet(AggregateReference<Pet, Integer> pet) {
        this.pet = pet;
    }

    /**
	 * Creates a new instance of Visit for the current date
	 */
	public Visit() {
		this.date = LocalDate.now();
	}

	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPetId() {
		return this.petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public Long getId() {
		return id;
	}

	public boolean isNew() {
		return id == null;
	}

	@Override
	public String toString() {
		return "Visit{" + "id=" + id + ", date=" + date + ", description='" + description + '\'' + ", petId=" + petId + '}';
	}
}
