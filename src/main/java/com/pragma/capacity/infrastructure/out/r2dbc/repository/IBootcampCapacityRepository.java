package com.pragma.capacity.infrastructure.out.r2dbc.repository;

import com.pragma.capacity.infrastructure.out.r2dbc.entity.BootcampCapacityEntity;
import com.pragma.capacity.infrastructure.out.r2dbc.projection.BootcampCapacityProjection;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IBootcampCapacityRepository extends ReactiveCrudRepository<BootcampCapacityEntity, Long> {

    @Query("""
            SELECT bc.bootcamp_id AS bootcamp_id, c.capacity_id AS capacity_id, c.name AS name, c.description AS description
            FROM bootcamp_capacity bc
            JOIN capacity c ON c.capacity_id = bc.capacity_id
            WHERE bc.bootcamp_id IN (:bootcampIds)
            """)
    Flux<BootcampCapacityProjection> findCapacitiesByBootcampIds(List<Long> bootcampIds);
}
