package com.pragma.capacity.domain;

import com.pragma.capacity.domain.model.CapacityModel;
import com.pragma.capacity.domain.spi.ICapacityPersistencePort;
import com.pragma.capacity.domain.usecase.CapacityUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CapacityUseCaseTest {

    private final ICapacityPersistencePort port = Mockito.mock(ICapacityPersistencePort.class);
    private final CapacityUseCase useCase = new CapacityUseCase(port);

    @Test
    void saveCapacityDelegatesToPort() {
        CapacityModel model = new CapacityModel(null, "test");
        Mockito.when(port.saveCapacity(model)).thenReturn(Mono.just(model));

        StepVerifier.create(useCase.saveCapacity(model)).expectNext(model).verifyComplete();
    }

    @Test
    void getAllCapacitysReturnsFlux() {
        CapacityModel model = new CapacityModel(null, "test");
        Mockito.when(port.getAllCapacitys()).thenReturn(Flux.just(model));

        StepVerifier.create(useCase.getAllCapacitys()).expectNext(model).verifyComplete();
    }
}
