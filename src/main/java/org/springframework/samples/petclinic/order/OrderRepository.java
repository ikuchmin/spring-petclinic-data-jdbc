package org.springframework.samples.petclinic.order;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
;
public interface OrderRepository extends ListCrudRepository<Order, Long>,
    PagingAndSortingRepository<Order, Long> {

}
