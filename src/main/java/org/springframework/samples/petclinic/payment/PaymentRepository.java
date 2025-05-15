package org.springframework.samples.petclinic.payment;

import org.springframework.data.repository.ListCrudRepository;

public interface PaymentRepository extends ListCrudRepository<Payment, Long> {
}
