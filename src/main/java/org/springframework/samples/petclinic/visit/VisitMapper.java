package org.springframework.samples.petclinic.visit;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface VisitMapper {

    Visit toEntity(VisitChangedEvent visitChangedEvent);

    VisitChangedEvent toVisitChangedEvent(Visit visit);

    default <T, R> AggregateReference<T, R> mapToAggregateReference(R id) {
        if (id == null) {
            return null;
        }
        return AggregateReference.to(id);
    }

    default <T, R> R mapFromAggregateReference(AggregateReference<T, R> aggregateReference) {
        if (aggregateReference == null) {
            return null;
        }
        return aggregateReference.getId();
    }
}
