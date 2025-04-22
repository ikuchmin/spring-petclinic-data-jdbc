package org.springframework.samples.petclinic.visit;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(profiles = "postgres")
@Sql(statements = "delete from visit; delete from pet; delete from owner", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class VisitRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(VisitRepositoryTest.class);
    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private OwnerRepository ownerRepository;


    @Test
    @Sql(scripts = "visit_with_owner_and_pet.sql")
    void findVisit() {
        Visit visit = visitRepository.findById(3L);
        log.info("Getting visit {} with pet {}", visit, visit.getPet());
    }

    @Test
    @Sql(scripts = "visit_with_owner_and_pet.sql")
    void referenceOnPartOfAgregate() {
        Owner owner = ownerRepository.findById(1);

        Pet punsh = new Pet();
        punsh.setName("Punsh");

        owner.getPets().add(punsh);

        ownerRepository.save(owner);
    }
}
