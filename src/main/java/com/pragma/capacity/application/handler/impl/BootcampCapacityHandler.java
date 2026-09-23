package com.pragma.capacity.application.handler.impl;

import com.pragma.capacity.application.dto.request.BootcampCapacitiesRequestDto;
import com.pragma.capacity.application.dto.response.BootcampCapacitiesResponseDto;
import com.pragma.capacity.application.handler.IBootcampCapacityHandler;
import com.pragma.capacity.application.mapper.ICapacityResponseMapper;
import com.pragma.capacity.domain.api.IBootcampCapacityServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BootcampCapacityHandler implements IBootcampCapacityHandler {

    private final IBootcampCapacityServicePort bootcampCapacityServicePort;
    private final ICapacityResponseMapper capacityResponseMapper;


    @Override
    public Mono<Void> saveBootcampCapacities(BootcampCapacitiesRequestDto bootcampCapacitiesRequestDto) {
        return bootcampCapacityServicePort.saveBootcampCapacities(
                bootcampCapacitiesRequestDto.bootcampId(),
                bootcampCapacitiesRequestDto.capacityIds());
    }

    @Override
    public Flux<BootcampCapacitiesResponseDto> getCapacitiesByBootcampIds(List<Long> bootcampIds) {
        return bootcampCapacityServicePort.getCapacitiesByBootcampIds(bootcampIds)
                .map(capacityResponseMapper::toResponse);
    }

    @Override
    public Mono<Void> deleteBootcamp(Long bootcampId) {
        return bootcampCapacityServicePort.delete(bootcampId);
    }
}
