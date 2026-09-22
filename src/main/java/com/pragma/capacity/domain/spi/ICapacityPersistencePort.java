package com.pragma.capacity.domain.spi;

import com.pragma.capacity.domain.model.CapacityModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapacityPersistencePort {
    Mono<CapacityModel> saveCapacity(CapacityModel capacityModel);

    Flux<CapacityModel> getAllCapacities();
}
