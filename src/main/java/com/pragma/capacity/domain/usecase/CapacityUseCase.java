package com.pragma.capacity.domain.usecase;

import com.pragma.capacity.domain.api.ICapacityServicePort;
import com.pragma.capacity.domain.model.CapacityModel;
import com.pragma.capacity.domain.spi.ICapacityPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CapacityUseCase implements ICapacityServicePort {

    private final ICapacityPersistencePort capacityPersistencePort;

    public CapacityUseCase(ICapacityPersistencePort capacityPersistencePort) {
        this.capacityPersistencePort = capacityPersistencePort;
    }

    @Override
    public Mono<CapacityModel> saveCapacity(CapacityModel capacityModel) {
        return capacityPersistencePort.saveCapacity(capacityModel);
    }

    @Override
    public Flux<CapacityModel> getAllCapacitys() {
        return capacityPersistencePort.getAllCapacitys();
    }
}
