package org.springframework.samples.petclinic.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;

public interface CriteriaOrderRepository {
    Page<Order> findAllWithFiltering(Criteria criteria, Pageable pageable);
}
