package com.pragma.capacity.domain.api;

import com.pragma.capacity.domain.model.BootcampCapacitiesModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampCapacityServicePort {

    Mono<Void> saveBootcampCapacities(Long bootcampId, List<Long> capacityIds);

    Flux<BootcampCapacitiesModel> getCapacitiesByBootcampIds(List<Long> bootcampIds);

    Mono<Void> delete(Long bootcampId);
}
