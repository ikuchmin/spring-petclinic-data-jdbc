package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(profiles = "postgres")
@Sql(statements = "delete from pet; delete from owner", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class OwnerRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(OwnerRepositoryTest.class);

    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    @Sql(statements = "delete from pet; delete from owner", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void checkCreatingOwnerWithPets() {
        Owner owner = new Owner();
        owner.setFirstName("Nick");
        owner.setLastName("Anderson");

        Pet puppy = new Pet();
        puppy.setName("Puppy");

        Pet fluffy = new Pet();
        fluffy.setName("Fluffy");

        //owner.setPets(List.of(puppy, fluffy));

        ownerRepository.save(owner);

        Collection<Owner> owners = ownerRepository.findByLastName("Anderson");

        log.info("Loaded owners: {}", owners);
    }

    @Test
    @Sql(scripts = "owner_with_pet.sql")
    void reorderingCollectionToAnalyzeWhatHappening() {
        Owner owner = ownerRepository.findById(1);
        //owner.getPets().sort(Comparator.comparing(Pet::getName));

        ownerRepository.save(owner);

        Owner againOwner = ownerRepository.findById(1);

        assertEquals(List.of(3, 2, 4), againOwner.getPets().stream().map(Pet::getId).toList());
    }

    @Test
    @Sql(scripts = "owner_with_pet.sql")
    void tryToAddNewPet() {
        Owner owner = ownerRepository.findById(1);

        Pet punsh = new Pet();
        punsh.setName("Punsh");

        owner.getPets().add(punsh);

        ownerRepository.save(owner);
        ownerRepository.save(owner);

        Owner againOwner = ownerRepository.findById(1);

        assertEquals(List.of(2, 3, 4, 5), againOwner.getPets().stream().map(Pet::getId).toList());
    }

    @Test
    @Sql(scripts = "owner_with_pet.sql")
    @Sql(statements = "delete from pet; delete from owner", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void checkReadingData() {
        Owner owner = ownerRepository.findById(1);

        log.info("Loaded owner: {}", owner);

        assertEquals(1, owner.getId());
        assertEquals(2, owner.getPets().size());
        assertEquals(Set.of(2, 3), owner.getPets().stream().map(Pet::getId).collect(Collectors.toSet()));
    }
}
