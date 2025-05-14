package org.springframework.samples.petclinic.service;

import org.springframework.data.repository.ListCrudRepository;

public interface ServiceRepository extends ListCrudRepository<Service, Long> {
}
