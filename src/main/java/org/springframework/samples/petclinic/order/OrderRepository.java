package org.springframework.samples.petclinic.order;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends ListCrudRepository<Order, Long>,
    ListPagingAndSortingRepository<Order, Long>, OrderCriteriaRepository {

    @Modifying
    @Query("UPDATE order_ SET status = :setStatus WHERE last_modified_date < :expirationDate AND status = :queryStatus")
    Integer changeStatusByModifiedDateBeforeAndStatus(OrderStatus setStatus, LocalDateTime expirationDate, OrderStatus queryStatus);

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
