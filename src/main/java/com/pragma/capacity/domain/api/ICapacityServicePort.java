package com.pragma.capacity.domain.api;

import com.pragma.capacity.domain.model.BootcampCapacitiesModel;
import com.pragma.capacity.domain.model.CapacityModel;
import com.pragma.capacity.domain.util.enums.CapacitySortBy;
import com.pragma.capacity.domain.util.PagedResult;
import com.pragma.capacity.domain.util.enums.SortDirection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapacityServicePort {

    Mono<CapacityModel> saveCapacity(CapacityModel capacityModel, List<Long> technologyIds);

    Mono<PagedResult<CapacityModel>> getAllCapacities(int page, int size, CapacitySortBy sortBy, SortDirection direction);

    Mono<Void> saveBootcampCapacities(Long bootcampId, List<Long> capacityIds);

    Flux<BootcampCapacitiesModel> getCapacitiesByBootcampIds(List<Long> bootcampIds);
}
