package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(profiles = "postgres")
class ServiceRepositoryTest {

    @Autowired
    private ServiceRepository serviceRepository;

    @Test
    void tryToCreateService() {
        Service service = new Service();
        service.setName("Test Service");

        Service save = serviceRepository.save(service);
        assertEquals(ServiceStatus.INACTIVE, save.getStatus());
    }
}
