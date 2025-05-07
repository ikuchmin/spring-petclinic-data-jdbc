package org.springframework.samples.petclinic.order;

import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class OrderExtendedDtoRowMapper implements RowMapper<OrderExtendedDto> {
    @Override
    public OrderExtendedDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        // First row processing setup
        if (rowNum == 0) {

            var id = rs.getLong("id");
            var totalCost = rs.getBigDecimal("total_cost");
            var createdDate = rs.getObject("created_date", LocalDateTime.class);
            var owner = new OrderExtendedDto.OwnerDto(
                rs.getInt("owner_id"),
                rs.getString("owner_firstName"),
                rs.getString("owner_lastName"),
                rs.getString("owner_telephone")
            );

            var pet = new OrderExtendedDto.PetDto(
                rs.getInt("pet_id"),
                rs.getString("pet_name"),
                rs.getObject("pet_birthDate", LocalDate.class),
                rs.getString("pet_typeName")
            );

            // Initialize collections
            Map<Long, OrderItemDtoWithOrder> orderItems = new HashMap<>();
            Map<Long, OrderExtendedDto.DiscountDto> discounts = new HashMap<>();
            Map<Long, OrderExtendedDto.PaymentDto> payments = new HashMap<>();
            Map<Long, OrderExtendedDto.ServiceDto> services = new HashMap<>();

            // Fill collections from all result rows
            do {
                // Process services
                if (rs.getObject("service_id") != null) {
                    Long serviceId = rs.getLong("service_id");
                    if (!services.containsKey(serviceId)) {
                        services.put(serviceId, new OrderExtendedDto.ServiceDto(
                            serviceId,
                            rs.getString("service_name")
                        ));
                    }
                }

                // Process order items
                if (rs.getObject("order_item_id") != null) {
                    Long itemId = rs.getLong("order_item_id");
                    if (!orderItems.containsKey(itemId)) {
                        orderItems.put(itemId, new OrderItemDtoWithOrder(
                            rs.getInt("order_item_order__key"),
                            new OrderExtendedDto.OrderItemDto(
                                itemId,
                                services.get(rs.getLong("service_id")),
                                rs.getString("order_item_service_name"),
                                rs.getBigDecimal("order_item_price"),
                                rs.getInt("order_item_count"),
                                rs.getBigDecimal("order_item_discount_cost"),
                                rs.getString("order_item_discount_reason"),
                                rs.getBigDecimal("order_item_cost")
                            )));
                    }
                }

                // Process order discounts
                if (rs.getObject("discount_id") != null) {
                    Long discountId = rs.getLong("discount_id");
                    if (!discounts.containsKey(discountId)) {
                        discounts.put(discountId, new OrderExtendedDto.DiscountDto(
                            discountId,
                            rs.getString("discount_code"),
                            rs.getString("discount_description"),
                            rs.getBigDecimal("discount_percent")
                        ));
                    }
                }

                // Process order payments
                if (rs.getObject("payment_id") != null) {
                    Long paymentId = rs.getLong("payment_id");
                    if (!payments.containsKey(paymentId)) {
                        payments.put(paymentId, new OrderExtendedDto.PaymentDto(
                            paymentId,
                            rs.getString("payment_method"),
                            rs.getObject("payment_paidAt", LocalDateTime.class),
                            rs.getBigDecimal("payment_amount")
                        ));
                    }
                }
            } while (rs.next());

            rs.next();

            List<OrderExtendedDto.OrderItemDto> sortedOrderItems = orderItems.values().stream()
                .sorted(Comparator.comparingInt(OrderItemDtoWithOrder::orderKey))
                .map(OrderItemDtoWithOrder::orderItem)
                .toList();

            // Create and return the OrderExtendedDto
            return new OrderExtendedDto(id, totalCost, createdDate,
                owner, pet,
                sortedOrderItems,
                new HashSet<>(discounts.values()),
                new HashSet<>(payments.values()));
        }

        // For subsequent rows, we'll never get here because we've consumed the entire result set
        return null;
    }

    record OrderItemDtoWithOrder(Integer orderKey, OrderExtendedDto.OrderItemDto orderItem) {
    }
}
