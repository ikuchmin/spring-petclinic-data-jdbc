package org.springframework.samples.petclinic.service;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ServiceMapper {
    Service toEntity(ServiceDto serviceDto);

    ServiceDto toServiceDto(Service service);
}
