package com.pragma.capacity.domain.usecase;

import com.pragma.capacity.domain.api.ICapacityServicePort;
import com.pragma.capacity.domain.exception.DuplicateTechnologyException;
import com.pragma.capacity.domain.exception.InvalidPaginationParameterException;
import com.pragma.capacity.domain.exception.InvalidTechnologyCountException;
import com.pragma.capacity.domain.model.CapacityModel;
import com.pragma.capacity.domain.model.CapacityTechnologies;
import com.pragma.capacity.domain.model.TechnologyModel;
import com.pragma.capacity.domain.spi.ICapacityPersistencePort;
import com.pragma.capacity.domain.spi.ITechnologyClientPort;
import com.pragma.capacity.domain.util.DomainConstants;
import com.pragma.capacity.domain.util.PagedResult;
import com.pragma.capacity.domain.util.enums.CapacitySortBy;
import com.pragma.capacity.domain.util.enums.SortDirection;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import static com.pragma.capacity.domain.util.enums.CapacitySortBy.NAME;

@Transactional
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


    @Override
    public Mono<PagedResult<CapacityModel>> getAllCapacities(int page, int size, CapacitySortBy sortBy, SortDirection direction) {
        if (page < 0 || size < 1) {
            throw new InvalidPaginationParameterException();
        }

        return sortBy == NAME
                ? getPageSortedByName(page, size, direction)
                : getPageSortedByTechnologyCount(page, size, direction);
    }

    private Mono<PagedResult<CapacityModel>> getPageSortedByName(int page, int size, SortDirection direction) {
        return capacityPersistencePort.getCapacitiesPageSortedByName(page, size, direction)
                .collectList()
                .zipWith(capacityPersistencePort.countCapacities())
                .flatMap(pageAndCount -> attachTechnologies(pageAndCount.getT1())
                        .map(withTechnologies -> toPagedResult(withTechnologies, page, size, pageAndCount.getT2())));
    }

    private Mono<PagedResult<CapacityModel>> getPageSortedByTechnologyCount(int page, int size, SortDirection direction) {
        return capacityPersistencePort.getAllCapacities()
                .collectList()
                .flatMap(capacities -> attachTechnologies(capacities)
                        .map(withTechnologies -> buildPageSortedByTechnologyCount(withTechnologies, page, size, direction)));
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

    private Mono<List<CapacityModel>> attachTechnologies(List<CapacityModel> capacities) {
        List<Long> capacityIds = capacities.stream().map(CapacityModel::getId).toList();
        if (capacityIds.isEmpty()) {
            return Mono.just(capacities);
        }

        return technologyClientPort.getTechnologiesByCapacityIds(capacityIds)
                .collectMap(CapacityTechnologies::capacityId, CapacityTechnologies::technologies)
                .map(technologiesByCapacityId -> {
                    capacities.forEach(capacity -> {
                        List<TechnologyModel> technologies = technologiesByCapacityId.getOrDefault(capacity.getId(), List.of());
                        capacity.setTechnologies(technologies);
                    });
                    return capacities;
                });
    }

    private PagedResult<CapacityModel> buildPageSortedByTechnologyCount(List<CapacityModel> capacities, int page, int size,
                                                                         SortDirection direction) {
        Comparator<CapacityModel> comparator = Comparator.comparingInt(capacity -> capacity.getTechnologies().size());
        if (direction == SortDirection.DESC) {
            comparator = comparator.reversed();
        }

        List<CapacityModel> sorted = capacities.stream().sorted(comparator).toList();
        long totalElements = sorted.size();

        List<CapacityModel> pageContent = sorted.stream()
                .skip((long) page * size)
                .limit(size)
                .toList();

        return toPagedResult(pageContent, page, size, totalElements);
    }

    private PagedResult<CapacityModel> toPagedResult(List<CapacityModel> pageContent, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PagedResult<>(pageContent, page, size, totalElements, totalPages);
    }
}
