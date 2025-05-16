package org.springframework.samples.petclinic.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderScheduleService {

    private static final Logger log = LoggerFactory.getLogger(OrderScheduleService.class);
    private final OrderRepository orderRepository;

    @Value("${petclinic.order.expirationMonths}")
    private int expirationMonths;

    public OrderScheduleService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // This method is scheduled to run every day at 02:00
    @Scheduled(cron = "0 0 2 * * ?")
    public void cancelObsoleteOrders() {
        LocalDateTime expirationDate = LocalDateTime.now()
            .minusHours(2) // at midnight, compensate cron
            .minusMonths(expirationMonths);

        Integer canceled = orderRepository.changeStatusByModifiedDateBeforeAndStatus(
            OrderStatus.CANCELLED, expirationDate, OrderStatus.PENDING);

        log.info("Canceled {} orders", canceled);
    }
}
