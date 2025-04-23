package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles(profiles = "postgres")
@Sql(scripts = "/org/springframework/samples/petclinic/service/service.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "/org/springframework/samples/petclinic/service/service_after.sql", executionPhase = AFTER_TEST_METHOD)
class ServiceServiceTest {

    @Autowired
    private ServiceService serviceService;

    @Test
    void checkThatPriceCorrectlyCalculatedForNowByDefault() {
        var serviceId = 1L;
        ServiceDto serviceDto = serviceService.retrieveService(serviceId);

        assertEquals(75.00, serviceDto.price().doubleValue());
    }

    @Test
    void checkThatPriceCorrectlyCalculatedForPastValue() {
        var serviceId = 1L;
        var date = LocalDateTime.now().minusDays(7);
        ServiceDto serviceDto = serviceService.retriveServiceOnDate(serviceId, date);

        assertEquals(50.00, serviceDto.price().doubleValue());
    }



}
