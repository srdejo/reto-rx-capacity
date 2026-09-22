package com.pragma.capacity.application.handler.impl;

import com.pragma.capacity.application.dto.request.BootcampCapacitiesRequestDto;
import com.pragma.capacity.application.dto.request.CapacityRequestDto;
import com.pragma.capacity.application.dto.response.BootcampCapacitiesResponseDto;
import com.pragma.capacity.application.dto.response.CapacityResponseDto;
import com.pragma.capacity.application.dto.response.PagedResponseDto;
import com.pragma.capacity.application.handler.ICapacityHandler;
import com.pragma.capacity.application.mapper.ICapacityRequestMapper;
import com.pragma.capacity.application.mapper.ICapacityResponseMapper;
import com.pragma.capacity.domain.api.ICapacityServicePort;
import com.pragma.capacity.domain.exception.InvalidPaginationParameterException;
import com.pragma.capacity.domain.util.enums.CapacitySortBy;
import com.pragma.capacity.domain.util.enums.SortDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CapacityHandler implements ICapacityHandler {

    private final ICapacityServicePort capacityServicePort;
    private final ICapacityRequestMapper capacityRequestMapper;
    private final ICapacityResponseMapper capacityResponseMapper;

    @Override
    public Mono<Void> saveCapacity(CapacityRequestDto capacityRequestDto) {
        return capacityServicePort.saveCapacity(
                capacityRequestMapper.toCapacity(capacityRequestDto),
                capacityRequestDto.getTechnologyIds()
        ).then();
    }

    @Override
    public Mono<PagedResponseDto<CapacityResponseDto>> getAllCapacities(int page, int size, String sortBy, String direction) {
        CapacitySortBy capacitySortBy = parseEnum(CapacitySortBy.class, toEnumName(sortBy));
        SortDirection sortDirection = parseEnum(SortDirection.class, toEnumName(direction));

        return capacityServicePort.getAllCapacities(page, size, capacitySortBy, sortDirection)
                .map(pagedResult -> new PagedResponseDto<>(
                        pagedResult.content().stream().map(capacityResponseMapper::toResponse).toList(),
                        pagedResult.page(),
                        pagedResult.size(),
                        pagedResult.totalElements(),
                        pagedResult.totalPages()
                ));
    }

    private <E extends Enum<E>> E parseEnum(Class<E> enumType, String value) {
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException _) {
            throw new InvalidPaginationParameterException();
        }
    }

    private String toEnumName(String value) {
        return value.replaceAll("([a-z])([A-Z])", "$1_$2")
                .replace("-", "_")
                .toUpperCase();
    }

    @Override
    public Mono<Void> saveBootcampCapacities(BootcampCapacitiesRequestDto bootcampCapacitiesRequestDto) {
        return capacityServicePort.saveBootcampCapacities(
                bootcampCapacitiesRequestDto.bootcampId(),
                bootcampCapacitiesRequestDto.capacityIds());
    }

    @Override
    public Flux<BootcampCapacitiesResponseDto> getCapacitiesByBootcampIds(List<Long> bootcampIds) {
        return capacityServicePort.getCapacitiesByBootcampIds(bootcampIds)
                .map(capacityResponseMapper::toResponse);
    }
}
