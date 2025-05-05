package org.springframework.samples.petclinic.order;

import org.apache.commons.collections4.ListUtils;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Order}
 */
public record OrderFilter(Double totalCostGt, LocalDateTime createdDateAfter,
                          LocalDateTime lastModifiedDateAfter, List<Integer> ownerIdsIn) {

    public Criteria toCriteria() {
        return Criteria.from(totalCostGtCriteria())
            .and(createdDateAfterCriteria())
            .and(lastModifiedDateAfterCriteria())
            .and(ownerNameContainsCriteria());
    }

    private Criteria totalCostGtCriteria() {
        if (totalCostGt != null) {
            return Criteria.where("total_cost").greaterThan(totalCostGt);
        }

        return Criteria.empty();
    }

    private Criteria createdDateAfterCriteria() {
        if (createdDateAfter != null) {
            return Criteria.where("created_date").greaterThan(createdDateAfter);
        }

        return Criteria.empty();
    }

    private Criteria lastModifiedDateAfterCriteria() {
        if (lastModifiedDateAfter != null) {
            return Criteria.where("last_modified_date").greaterThan(lastModifiedDateAfter);
        }

        return Criteria.empty();
    }

    private Criteria ownerNameContainsCriteria() {
        if (ownerIdsIn != null && ! ownerIdsIn.isEmpty()) {
            return Criteria.where("owner").in(ownerIdsIn);
        }

        return Criteria.empty();
    }
}
