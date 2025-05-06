package org.springframework.samples.petclinic.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles(profiles = "postgres")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Sql(scripts = "/org/springframework/samples/petclinic/order/order_analyse_report.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/org/springframework/samples/petclinic/order/order_analyse_report_delete.sql", executionPhase = AFTER_TEST_METHOD)
    void checkThatReportReturnsDataWithParams() {

        LocalDateTime now = LocalDateTime.now();
        List<OrderAnalyseReportDto> data = orderRepository.findDataForAnalyseReport(
            2, new BigDecimal("500.00"), now.minusDays(1), now.plusDays(1));

        assertEquals(1, data.size());
        assertEquals(1L, data.getFirst().id());
        assertEquals(575.00, data.getFirst().totalCost().doubleValue());
    }

}
