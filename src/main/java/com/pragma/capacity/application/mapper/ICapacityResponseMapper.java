package com.pragma.capacity.application.mapper;

import com.pragma.capacity.application.dto.response.BootcampCapacitiesResponseDto;
import com.pragma.capacity.application.dto.response.CapacityResponseDto;
import com.pragma.capacity.application.dto.response.TechnologyResponseDto;
import com.pragma.capacity.domain.model.BootcampCapacitiesModel;
import com.pragma.capacity.domain.model.CapacityModel;
import com.pragma.capacity.domain.model.TechnologyModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapacityResponseMapper {
    CapacityResponseDto toResponse(CapacityModel capacityModel);

    TechnologyResponseDto toResponse(TechnologyModel technologyModel);

    BootcampCapacitiesResponseDto toResponse(BootcampCapacitiesModel bootcampCapacitiesModel);
}
