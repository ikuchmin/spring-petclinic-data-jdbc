package org.springframework.samples.petclinic.discount;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface DiscountRepository extends ListCrudRepository<Discount, Long> {
    Optional<Discount> findByCode(String code);
}
