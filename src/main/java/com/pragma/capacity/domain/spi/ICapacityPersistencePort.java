package com.pragma.capacity.domain.spi;

import com.pragma.capacity.domain.model.CapacityModel;
import com.pragma.capacity.domain.util.enums.SortDirection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapacityPersistencePort {
    Mono<CapacityModel> saveCapacity(CapacityModel capacityModel);

    Flux<CapacityModel> getAllCapacities();

    Flux<CapacityModel> getCapacitiesPageSortedByName(int page, int size, SortDirection direction);

    Mono<Long> countCapacities();

    Flux<Long> findExistingCapacityIds(List<Long> capacityIds);

    Mono<Void> deleteCapacitiesByIds(List<Long> capacityIds);
}
