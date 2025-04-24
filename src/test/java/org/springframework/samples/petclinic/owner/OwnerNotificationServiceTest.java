package org.springframework.samples.petclinic.owner;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles(profiles = "postgres")
class OwnerNotificationServiceTest {

    @Autowired
    private VisitRepository visitRepository;

    @Test
    @Sql(scripts = "/org/springframework/samples/petclinic/visit/visit.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/org/springframework/samples/petclinic/visit/visit_delete.sql", executionPhase = AFTER_TEST_METHOD)
    void checkEventOnAddingVisitSentAndRetrieved() throws InterruptedException {
        var ownerId = 1;
        var petId = 1;

        Thread.sleep(10000);

        Visit visit = new Visit();
        visit.setOwner(AggregateReference.to(ownerId));
        visit.setPet(AggregateReference.to(petId));
        visit.setDescription("test");
        visit.setDate(java.time.LocalDate.now());

        visitRepository.save(visit);

        Thread.sleep(10000);
    }
}
