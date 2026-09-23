package com.pragma.capacity.infrastructure.out.r2dbc.adapter;

import com.pragma.capacity.domain.model.CapacityModel;
import com.pragma.capacity.domain.spi.ICapacityPersistencePort;
import com.pragma.capacity.domain.util.enums.SortDirection;
import com.pragma.capacity.infrastructure.exception.NoDataFoundException;
import com.pragma.capacity.infrastructure.out.r2dbc.mapper.ICapacityEntityMapper;
import com.pragma.capacity.infrastructure.out.r2dbc.repository.ICapacityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class CapacityAdapter implements ICapacityPersistencePort {

    private final ICapacityRepository capacityRepository;
    private final ICapacityEntityMapper capacityEntityMapper;

    @Override
    public Mono<CapacityModel> saveCapacity(CapacityModel capacityModel) {
        return capacityRepository.save(capacityEntityMapper.toEntity(capacityModel))
                .map(capacityEntityMapper::toCapacityModel);
    }

    @Override
    public Flux<CapacityModel> getAllCapacities() {
        return capacityRepository.findAll()
                .switchIfEmpty(Flux.error(new NoDataFoundException()))
                .map(capacityEntityMapper::toCapacityModel);
    }

    @Override
    public Flux<CapacityModel> getCapacitiesPageSortedByName(int page, int size, SortDirection direction) {
        Sort.Direction sortDirection = direction == SortDirection.DESC ? Sort.Direction.DESC : Sort.Direction.ASC;
        return capacityRepository.findAllBy(PageRequest.of(page, size, Sort.by(sortDirection, "name")))
                .map(capacityEntityMapper::toCapacityModel);
    }

    @Override
    public Mono<Long> countCapacities() {
        return capacityRepository.count();
    }

    @Override
    public Flux<Long> findExistingCapacityIds(List<Long> capacityIds) {
        return capacityRepository.findAllById(capacityIds)
                .map(capacityEntityMapper::toCapacityModel)
                .map(CapacityModel::getId);
    }

    @Override
    public Mono<Void> deleteCapacitiesByIds(List<Long> capacityIds) {
        return capacityRepository.deleteAllById(capacityIds);
    }
}
