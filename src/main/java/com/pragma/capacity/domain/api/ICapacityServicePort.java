package com.pragma.capacity.domain.api;

import com.pragma.capacity.domain.model.CapacityModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapacityServicePort {

    Mono<CapacityModel> saveCapacity(CapacityModel capacityModel, List<Long> technologyIds);

    Flux<CapacityModel> getAllCapacities();
}
