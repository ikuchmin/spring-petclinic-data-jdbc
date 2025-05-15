package org.springframework.samples.petclinic.order;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.samples.petclinic.discount.Discount;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DiscountMapper {
    Discount toEntity(DiscountCodeDto discountCodeDto);

    DiscountCodeDto toDiscountCodeDto(Discount discount);
}
