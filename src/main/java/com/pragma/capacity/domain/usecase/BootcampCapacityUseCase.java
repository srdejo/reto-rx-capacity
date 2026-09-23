package com.pragma.capacity.domain.usecase;

import com.pragma.capacity.domain.api.IBootcampCapacityServicePort;
import com.pragma.capacity.domain.exception.CapacityNotFoundException;
import com.pragma.capacity.domain.model.BootcampCapacitiesModel;
import com.pragma.capacity.domain.model.CapacityModel;
import com.pragma.capacity.domain.spi.IBootcampCapacityPersistencePort;
import com.pragma.capacity.domain.spi.ICapacityPersistencePort;
import com.pragma.capacity.domain.spi.ITechnologyClientPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.pragma.capacity.domain.util.CapacityTechnologyUtils.attachTechnologies;

public class BootcampCapacityUseCase implements IBootcampCapacityServicePort {

    private final ICapacityPersistencePort capacityPersistencePort;
    private final ITechnologyClientPort technologyClientPort;
    private final IBootcampCapacityPersistencePort bootcampCapacityPersistencePort;

    public BootcampCapacityUseCase(ICapacityPersistencePort capacityPersistencePort, ITechnologyClientPort technologyClientPort,
                                   IBootcampCapacityPersistencePort bootcampCapacityPersistencePort) {
        this.capacityPersistencePort = capacityPersistencePort;
        this.technologyClientPort = technologyClientPort;
        this.bootcampCapacityPersistencePort = bootcampCapacityPersistencePort;
    }

    @Override
    public Mono<Void> saveBootcampCapacities(Long bootcampId, List<Long> capacityIds) {
        return capacityPersistencePort.findExistingCapacityIds(capacityIds)
                .collectList()
                .flatMap(existingIds -> {
                    if (existingIds.size() != capacityIds.size()) {
                        return Mono.error(new CapacityNotFoundException());
                    }
                    return bootcampCapacityPersistencePort.saveBootcampCapacities(bootcampId, capacityIds);
                });
    }

    @Override
    public Flux<BootcampCapacitiesModel> getCapacitiesByBootcampIds(List<Long> bootcampIds) {
        return bootcampCapacityPersistencePort.getCapacitiesByBootcampIds(bootcampIds)
                .collectList()
                .flatMapMany(this::attachTechnologiesToBootcampCapacities);
    }

    @Override
    public Mono<Void> delete(Long bootcampId) {
        return bootcampCapacityPersistencePort.getCapacitiesByBootcampId(bootcampId)
                .map(BootcampCapacitiesModel::capacities)
                .map(capacities -> capacities.stream().map(CapacityModel::getId).toList())
                .flatMap(capacityIds -> bootcampCapacityPersistencePort.delete(bootcampId)
                        .then(deleteOrphanCapacities(capacityIds)));
    }

    private Mono<Void> deleteOrphanCapacities(List<Long> capacityIds) {
        if (capacityIds.isEmpty()) {
            return Mono.empty();
        }

        return bootcampCapacityPersistencePort.findReferencedCapacityIds(capacityIds)
                .collectList()
                .flatMap(stillReferencedIds -> {
                    List<Long> orphanCapacityIds = capacityIds.stream()
                            .filter(capacityId -> !stillReferencedIds.contains(capacityId))
                            .toList();
                    if (orphanCapacityIds.isEmpty()) {
                        return Mono.empty();
                    }
                    return capacityPersistencePort.deleteCapacitiesByIds(orphanCapacityIds)
                            .then(technologyClientPort.deleteTechnologiesByCapacityIds(orphanCapacityIds));
                });
    }

    private Flux<BootcampCapacitiesModel> attachTechnologiesToBootcampCapacities(List<BootcampCapacitiesModel> bootcampCapacities) {
        List<CapacityModel> capacities = bootcampCapacities.stream()
                .flatMap(bootcampCapacity -> bootcampCapacity.capacities().stream())
                .toList();

        return attachTechnologies(capacities, technologyClientPort)
                .thenMany(Flux.fromIterable(bootcampCapacities));
    }
}
