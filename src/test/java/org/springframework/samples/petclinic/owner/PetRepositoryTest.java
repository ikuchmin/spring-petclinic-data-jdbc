package org.springframework.samples.petclinic.owner;

import org.checkerframework.common.value.qual.IntRange;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles(profiles = "postgres")
class PetRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(PetRepositoryTest.class);
    @Autowired
    private CrudPetRepository crudPetRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    static int endExclusive = 10000;

    @Test
    void batchTest() {

        long startCount = crudPetRepository.count();

        long start = System.currentTimeMillis();

        List<Pet> pets = IntStream.range(1, endExclusive)
            .mapToObj(n -> new Pet()).toList();

        crudPetRepository.saveAll(pets);

        log.info("batch insert in data: {}", System.currentTimeMillis() - start);

        assertEquals(startCount + (endExclusive - 1), crudPetRepository.count());
    }

    @Test
    void jdbcTemplateBatch() {

        long startCount = crudPetRepository.count();

        long start = System.currentTimeMillis();

        var petSql = "INSERT INTO pet(name) VALUES " + IntStream.range(1, endExclusive)
            .mapToObj(n -> "(null)")
            .collect(Collectors.joining(","));

        jdbcTemplate.batchUpdate(petSql);

        log.info("batch insert in template: {}", System.currentTimeMillis() - start);

        assertEquals(startCount + (endExclusive - 1), crudPetRepository.count());
    }
}
