package org.springframework.samples.petclinic.owner;

import org.springframework.data.repository.ListCrudRepository;

public interface CrudPetRepository extends ListCrudRepository<Pet, Integer> {
}
