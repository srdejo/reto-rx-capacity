package com.pragma.capacity.domain;

import com.pragma.capacity.domain.exception.InvalidTechnologyCountException;
import com.pragma.capacity.domain.model.CapacityModel;
import com.pragma.capacity.domain.spi.ICapacityPersistencePort;
import com.pragma.capacity.domain.spi.ITechnologyClientPort;
import com.pragma.capacity.domain.usecase.CapacityUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CapacityUseCaseTest {

    private final ICapacityPersistencePort port = mock(ICapacityPersistencePort.class);
    private final ITechnologyClientPort technologyClientPort = mock(ITechnologyClientPort.class);
    private final CapacityUseCase useCase = new CapacityUseCase(port, technologyClientPort);

    @Test
    void saveCapacityDelegatesToPort() {
        CapacityModel model = new CapacityModel(null, "test");
        List<Long> technologyIds = List.of(1L, 2L, 3L);

        when(port.saveCapacity(model)).thenReturn(Mono.just(model));
        when(technologyClientPort.associateTechnologies(model.getId(), technologyIds)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.saveCapacity(model, technologyIds)).expectNext(model).verifyComplete();

        verify(technologyClientPort).associateTechnologies(model.getId(), technologyIds);
    }

    @Test
    void saveCapacityThrowsWhenTechnologyCountIsBelowMinimum() {
        CapacityModel model = new CapacityModel(null, "test");
        List<Long> technologyIds = List.of(1L, 2L);

        assertThrows(InvalidTechnologyCountException.class, () -> useCase.saveCapacity(model, technologyIds));

        Mockito.verifyNoInteractions(port, technologyClientPort);
    }

    @Test
    void getAllCapacitysReturnsFlux() {
        CapacityModel model = new CapacityModel(null, "test");
        when(port.getAllCapacities()).thenReturn(Flux.just(model));

        StepVerifier.create(useCase.getAllCapacities()).expectNext(model).verifyComplete();
    }
}
