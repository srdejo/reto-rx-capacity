package com.pragma.capacity.application.handler;

import com.pragma.capacity.application.dto.request.BootcampCapacitiesRequestDto;
import com.pragma.capacity.application.dto.request.CapacityRequestDto;
import com.pragma.capacity.application.dto.response.BootcampCapacitiesResponseDto;
import com.pragma.capacity.application.dto.response.CapacityResponseDto;
import com.pragma.capacity.application.dto.response.PagedResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapacityHandler {

    Mono<Void> saveCapacity(CapacityRequestDto capacityRequestDto);

    Mono<PagedResponseDto<CapacityResponseDto>> getAllCapacities(int page, int size, String sortBy, String direction);

    Mono<Void> saveBootcampCapacities(BootcampCapacitiesRequestDto bootcampCapacitiesRequestDto);

    Flux<BootcampCapacitiesResponseDto> getCapacitiesByBootcampIds(List<Long> bootcampIds);
}
