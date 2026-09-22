package com.pragma.capacity.domain.usecase;

import com.pragma.capacity.domain.api.ICapacityServicePort;
import com.pragma.capacity.domain.exception.DuplicateTechnologyException;
import com.pragma.capacity.domain.exception.InvalidTechnologyCountException;
import com.pragma.capacity.domain.model.CapacityModel;
import com.pragma.capacity.domain.spi.ICapacityPersistencePort;
import com.pragma.capacity.domain.spi.ITechnologyClientPort;
import com.pragma.capacity.domain.util.DomainConstants;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;

public class CapacityUseCase implements ICapacityServicePort {

    private final ICapacityPersistencePort capacityPersistencePort;
    private final ITechnologyClientPort technologyClientPort;

    public CapacityUseCase(ICapacityPersistencePort capacityPersistencePort, ITechnologyClientPort technologyClientPort) {
        this.capacityPersistencePort = capacityPersistencePort;
        this.technologyClientPort = technologyClientPort;
    }

    @Override
    public Mono<CapacityModel> saveCapacity(CapacityModel capacityModel, List<Long> technologyIds) {
        validateTechnologies(technologyIds);

        return capacityPersistencePort.saveCapacity(capacityModel)
                .flatMap(savedCapacity ->
                        technologyClientPort
                                .associateTechnologies(
                                        savedCapacity.getId(),
                                        technologyIds
                                )
                                .thenReturn(savedCapacity)
                );
    }

    private void validateTechnologies(List<Long> technologyIds) {
        if (technologyIds.size() < DomainConstants.MIN_TECHNOLOGIES
                || technologyIds.size() > DomainConstants.MAX_TECHNOLOGIES) {
            throw new InvalidTechnologyCountException();
        }

        if (technologyIds.size() != new HashSet<>(technologyIds).size()) {
            throw new DuplicateTechnologyException();
        }
    }

    @Override
    public Flux<CapacityModel> getAllCapacities() {
        return capacityPersistencePort.getAllCapacities();
    }
}
