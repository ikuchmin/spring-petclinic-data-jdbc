package org.springframework.samples.petclinic.order;

import org.springframework.data.repository.ListCrudRepository;

public interface PaymentRepository extends ListCrudRepository<Payment, Long> {
}
