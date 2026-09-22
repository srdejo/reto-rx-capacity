package com.pragma.capacity.domain;

import com.pragma.capacity.domain.exception.InvalidTechnologyCountException;
import com.pragma.capacity.domain.model.CapacityModel;
import com.pragma.capacity.domain.model.CapacityTechnologies;
import com.pragma.capacity.domain.model.TechnologyModel;
import com.pragma.capacity.domain.spi.ICapacityPersistencePort;
import com.pragma.capacity.domain.spi.ITechnologyClientPort;
import com.pragma.capacity.domain.usecase.CapacityUseCase;
import com.pragma.capacity.domain.util.PagedResult;
import com.pragma.capacity.domain.util.enums.CapacitySortBy;
import com.pragma.capacity.domain.util.enums.SortDirection;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        verifyNoInteractions(port, technologyClientPort);
    }

    // Criterio: se debe poder parametrizar el orden (ascendente/descendente) por nombre o por cantidad de tecnologias.
    @Test
    void getAllCapacitiesSortsByTechnologyCountAscendingWhenRequested() {
        mockCapacitiesWithTechnologies();

        PagedResult<CapacityModel> result = useCase.getAllCapacities(0, 10, CapacitySortBy.TECHNOLOGY_COUNT, SortDirection.ASC)
                .block();

        assertEquals(List.of("Frontend", "Backend", "Data"),
                result.content().stream().map(CapacityModel::getName).toList());
    }

    // Criterio: el servicio debe estar paginado.
    @Test
    void getAllCapacitiesReturnsRequestedPage() {
        mockCapacitiesWithTechnologies();

        PagedResult<CapacityModel> result = useCase.getAllCapacities(0, 2, CapacitySortBy.NAME, SortDirection.ASC)
                .block();

        assertEquals(2, result.content().size());
        assertEquals(0, result.page());
        assertEquals(2, result.size());
        assertEquals(3, result.totalElements());
        assertEquals(2, result.totalPages());
    }

    // Criterio: cada capacidad listada debe traer sus tecnologias solo con id y nombre.
    @Test
    void getAllCapacitiesAttachesTechnologiesWithIdAndName() {
        mockCapacitiesWithTechnologies();

        PagedResult<CapacityModel> result = useCase.getAllCapacities(0, 10, CapacitySortBy.NAME, SortDirection.ASC)
                .block();

        CapacityModel backend = result.content().stream()
                .filter(capacity -> capacity.getId().equals(1L))
                .findFirst()
                .orElseThrow();

        assertEquals(
                List.of("10:Java", "11:Spring"),
                backend.getTechnologies().stream()
                        .map(technology -> technology.getId() + ":" + technology.getName())
                        .toList()
        );
    }

    private void mockCapacitiesWithTechnologies() {
        CapacityModel backend = new CapacityModel(1L, "Backend");
        CapacityModel frontend = new CapacityModel(2L, "Frontend");
        CapacityModel data = new CapacityModel(3L, "Data");

        when(port.getAllCapacities()).thenReturn(Flux.just(backend, frontend, data));

        when(technologyClientPort.getTechnologiesByCapacityIds(List.of(1L, 2L, 3L)))
                .thenReturn(Flux.just(
                        new CapacityTechnologies(1L, List.of(
                                new TechnologyModel(10L, "Java"),
                                new TechnologyModel(11L, "Spring"))),
                        new CapacityTechnologies(2L, List.of(
                                new TechnologyModel(12L, "React"))),
                        new CapacityTechnologies(3L, List.of(
                                new TechnologyModel(13L, "Python"),
                                new TechnologyModel(14L, "Spark"),
                                new TechnologyModel(15L, "Airflow")))
                ));
    }
}
