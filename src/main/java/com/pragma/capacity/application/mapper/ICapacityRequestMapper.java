package com.pragma.capacity.application.mapper;

import com.pragma.capacity.application.dto.request.CapacityRequestDto;
import com.pragma.capacity.domain.model.CapacityModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapacityRequestMapper {
    CapacityModel toCapacity(CapacityRequestDto capacityRequestDto);
}
