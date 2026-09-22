package com.pragma.capacity.domain.spi;

import com.pragma.capacity.domain.model.BootcampCapacitiesModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampCapacityPersistencePort {
    Mono<Void> saveBootcampCapacities(Long bootcampId, List<Long> capacityIds);

    Flux<BootcampCapacitiesModel> getCapacitiesByBootcampIds(List<Long> bootcampIds);
}
