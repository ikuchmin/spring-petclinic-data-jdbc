package org.springframework.samples.petclinic.order;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends ListCrudRepository<Order, Long>,
    PagingAndSortingRepository<Order, Long>, CriteriaOrderRepository {

    @Query(value = """
            SELECT o.id AS id, o.created_date AS created_date, o.total_cost AS total_cost,
                   ow.id AS owner_id, ow.first_name AS owner_firstName, ow.last_name AS owner_lastName, ow.telephone AS owner_telephone,
                   p.id AS pet_id, p.name AS pet_name, p.birth_date AS pet_birthDate, pt.name AS pet_typeName,
                   d.id AS discount_id, d.code AS discount_code, d.description as discount_description, d.percent AS discount_percent,
                   pm.id AS payment_id, pm.method AS payment_method, pm.paid_at AS payment_paidAt, pm.amount AS payment_amount, pm.message AS payment_message,
                   oi.id AS order_item_id, oi.service_name AS order_item_service_name, oi.price AS order_item_price, oi.count AS order_item_count,
                   oi.discount_cost AS order_item_discount_cost, oi.discount_reason AS order_item_discount_reason, oi.cost AS order_item_cost,
                   oi.discount_reason AS order_item_discount_reason, oi.cost AS order_item_cost, oi.order__key as order_item_order__key,
                   s.id AS service_id, s.name AS service_name
            FROM order_ o
            JOIN owner ow ON o.owner_id = ow.id
            JOIN pet p ON o.pet_id = p.id
            JOIN pet_type pt ON p.type_id = pt.id
            LEFT JOIN order_discount od ON od.order_ = o.id
            LEFT JOIN discount d ON od.discount_id = d.id
            LEFT JOIN order_payment op ON op.order_ = o.id
            LEFT JOIN payment pm ON op.payment_id = pm.id
            LEFT JOIN order_item oi ON oi.order_ = o.id
            LEFT JOIN service s ON oi.service_id = s.id
            WHERE o.id = :id
            """, rowMapperClass = OrderExtendedDtoRowMapper.class)
    Optional<OrderExtendedDto> findOrderExtendedById(Long id);

    @Query(value = """
            SELECT o.id AS id, o.created_date AS created_date, o.total_cost AS total_cost,
                   o.last_modified_date AS last_modified_date, count(oi.id) AS items_count
            FROM order_ o
            JOIN order_item oi on oi.order_ = o.id
            WHERE o.total_cost >= :minTotalCost
              AND o.created_date BETWEEN :minCreatedDate AND :maxCreatedDate
            GROUP BY o.id
            HAVING count(oi.id) >= :minCountOfItems;
            """)
    List<OrderAnalyseReportDto> findDataForAnalyseReport(Integer minCountOfItems, BigDecimal minTotalCost, LocalDateTime minCreatedDate, LocalDateTime maxCreatedDate);
}
