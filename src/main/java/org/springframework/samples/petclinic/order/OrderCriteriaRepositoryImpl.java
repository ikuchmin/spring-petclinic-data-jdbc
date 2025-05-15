package org.springframework.samples.petclinic.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.repository.Repository;

public class OrderCriteriaRepositoryImpl implements OrderCriteriaRepository {

    private final JdbcAggregateTemplate jdbcAggregateTemplate;

    public OrderCriteriaRepositoryImpl(JdbcAggregateTemplate jdbcAggregateTemplate) {
        this.jdbcAggregateTemplate = jdbcAggregateTemplate;
    }

    @Override
    public Page<Order> findAllWithFiltering(Criteria criteria, Pageable pageable) {
        return jdbcAggregateTemplate.findAll(Query.query(criteria), Order.class, pageable);
    }
}
