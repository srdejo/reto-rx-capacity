package com.pragma.capacity.infrastructure.out.r2dbc.adapter;

import com.pragma.capacity.domain.model.BootcampCapacitiesModel;
import com.pragma.capacity.domain.model.CapacityModel;
import com.pragma.capacity.domain.spi.IBootcampCapacityPersistencePort;
import com.pragma.capacity.infrastructure.out.r2dbc.entity.BootcampCapacityEntity;
import com.pragma.capacity.infrastructure.out.r2dbc.projection.BootcampCapacityProjection;
import com.pragma.capacity.infrastructure.out.r2dbc.repository.IBootcampCapacityRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BootcampCapacityAdapter implements IBootcampCapacityPersistencePort {

    private final IBootcampCapacityRepository bootcampCapacityRepository;

    @Override
    public Mono<Void> saveBootcampCapacities(Long bootcampId, List<Long> capacityIds) {
        return Flux.fromIterable(capacityIds)
                .map(capacityId -> new BootcampCapacityEntity(capacityId, bootcampId))
                .as(bootcampCapacityRepository::saveAll)
                .then();
    }

    @Override
    public Flux<BootcampCapacitiesModel> getCapacitiesByBootcampIds(List<Long> bootcampIds) {
        if (bootcampIds.isEmpty()) {
            return Flux.empty();
        }

        return bootcampCapacityRepository.findCapacitiesByBootcampIds(bootcampIds)
                .collectMultimap(BootcampCapacityProjection::bootcampId, this::toCapacityModel)
                .flatMapMany(capacitiesByBootcampId -> Flux.fromIterable(bootcampIds)
                        .map(bootcampId -> new BootcampCapacitiesModel(
                                bootcampId,
                                new ArrayList<>(capacitiesByBootcampId.getOrDefault(bootcampId, List.of())))));
    }

    private CapacityModel toCapacityModel(BootcampCapacityProjection projection) {
        return new CapacityModel(projection.capacityId(), projection.name(), projection.description(), null);
    }
}
