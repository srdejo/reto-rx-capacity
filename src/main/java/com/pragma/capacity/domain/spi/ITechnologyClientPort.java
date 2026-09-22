package com.pragma.capacity.domain.spi;

import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyClientPort {
    Mono<Void> associateTechnologies(Long capacityId, List<Long> technologyIds);
}
