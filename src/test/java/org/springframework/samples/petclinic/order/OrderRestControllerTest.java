package org.springframework.samples.petclinic.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link OrderRestController}
 */
@SpringBootTest
@ActiveProfiles(profiles = "postgres")
@AutoConfigureMockMvc
@Sql(scripts = "/org/springframework/samples/petclinic/order/order.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "/org/springframework/samples/petclinic/order/order_delete.sql", executionPhase = AFTER_TEST_METHOD)
public class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {

    }

    @Test
    public void getAllWithFiltering() throws Exception {
        mockMvc.perform(get("/rest/orders/filter")
                .param("totalCostGt", "12.50"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("content[0].id").value(1))
            .andDo(print());
    }

    @Test
    public void doNotfindAllWithFiltering() throws Exception {
        mockMvc.perform(get("/rest/orders/filter")
                .param("totalCostGt", "100.50"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("content").isEmpty())
            .andDo(print());
    }

    @Test
    public void getAllWithFilteringByOwnerId() throws Exception {
        mockMvc.perform(get("/rest/orders/filter")
                .param("ownerIdsIn", "1,2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("content[0].id").value(1))
            .andDo(print());
    }

    @Test
    public void findByCreatedDate() throws Exception {
        mockMvc.perform(get("/rest/orders/filter")
                .param("createdDateAfter", LocalDateTime.now().minusDays(1).toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("content[0].id").value(1))
            .andDo(print());
    }

    @Test
    public void findByCreatedDateNegative() throws Exception {
        mockMvc.perform(get("/rest/orders/filter")
                .param("createdDateAfter", LocalDateTime.now().plusDays(1).toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("content").isEmpty())
            .andDo(print());
    }

    @Test
    public void findByLastModifiedDate() throws Exception {
        mockMvc.perform(get("/rest/orders/filter")
                .param("lastModifiedDateAfter", LocalDateTime.now().minusDays(1).toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("content[0].id").value(1))
            .andDo(print());
    }

    @Test
    public void findByLastModifiedDateNegative() throws Exception {
        mockMvc.perform(get("/rest/orders/filter")
                .param("lastModifiedDateAfter", LocalDateTime.now().plusDays(1).toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("content").isEmpty())
            .andDo(print());
    }

    @Test
    public void findByFilterInCombination() throws Exception {
        mockMvc.perform(get("/rest/orders/filter")
                .param("totalCostGt", "12.50")
                .param("createdDateAfter", LocalDateTime.now().minusDays(1).toString())
                .param("lastModifiedDateAfter", LocalDateTime.now().minusDays(1).toString())
                .param("ownerIdsIn", "1,2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("content[0].id").value(1))
            .andDo(print());
    }

    @Test
    public void findByFilterInCombinationWithOneProblem() throws Exception {
        mockMvc.perform(get("/rest/orders/filter")
                .param("totalCostGt", "100.50")
                .param("createdDateAfter", LocalDateTime.now().minusDays(1).toString())
                .param("lastModifiedDateAfter", LocalDateTime.now().minusDays(1).toString())
                .param("ownerIdsIn", "1,2,3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("content").isEmpty())
            .andDo(print());
    }
}
