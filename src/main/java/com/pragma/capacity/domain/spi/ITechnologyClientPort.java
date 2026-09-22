package com.pragma.capacity.domain.spi;

import com.pragma.capacity.domain.model.CapacityTechnologies;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyClientPort {
    Mono<Void> associateTechnologies(Long capacityId, List<Long> technologyIds);

    Flux<CapacityTechnologies> getTechnologiesByCapacityIds(List<Long> capacityIds);
}
