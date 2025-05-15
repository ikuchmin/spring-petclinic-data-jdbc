package org.springframework.samples.petclinic.discount;

import org.springframework.data.repository.ListCrudRepository;

public interface DiscountRepository extends ListCrudRepository<Discount, Long> {
    Discount findByCode(String code);
}
