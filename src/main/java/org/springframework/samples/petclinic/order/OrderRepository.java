package org.springframework.samples.petclinic.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface OrderRepository extends ListCrudRepository<Order, Long>,
    ListPagingAndSortingRepository<Order, Long>, OrderCriteriaRepository {
}
