package com.pragma.capacity.domain.api;

import com.pragma.capacity.domain.model.CapacityModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapacityServicePort {

    Mono<CapacityModel> saveCapacity(CapacityModel capacityModel);

    Flux<CapacityModel> getAllCapacitys();
}
