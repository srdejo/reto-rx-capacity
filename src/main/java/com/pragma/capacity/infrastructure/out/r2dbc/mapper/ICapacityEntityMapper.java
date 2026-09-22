package com.pragma.capacity.infrastructure.out.r2dbc.mapper;

import com.pragma.capacity.domain.model.CapacityModel;
import com.pragma.capacity.infrastructure.out.r2dbc.entity.CapacityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface ICapacityEntityMapper {

    CapacityEntity toEntity(CapacityModel capacityModel);

    CapacityModel toCapacityModel(CapacityEntity capacityEntity);
}
