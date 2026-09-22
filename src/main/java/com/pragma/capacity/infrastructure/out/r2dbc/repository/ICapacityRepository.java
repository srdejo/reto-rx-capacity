package com.pragma.capacity.infrastructure.out.r2dbc.repository;

import com.pragma.capacity.infrastructure.out.r2dbc.entity.CapacityEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface ICapacityRepository extends ReactiveCrudRepository<CapacityEntity, Long>,
        ReactiveSortingRepository<CapacityEntity, Long> {

    Flux<CapacityEntity> findAllBy(Pageable pageable);
}
