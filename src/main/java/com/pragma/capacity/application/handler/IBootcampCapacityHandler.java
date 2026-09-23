package com.pragma.capacity.application.handler;

import com.pragma.capacity.application.dto.request.BootcampCapacitiesRequestDto;
import com.pragma.capacity.application.dto.response.BootcampCapacitiesResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampCapacityHandler {

    Mono<Void> saveBootcampCapacities(BootcampCapacitiesRequestDto bootcampCapacitiesRequestDto);

    Flux<BootcampCapacitiesResponseDto> getCapacitiesByBootcampIds(List<Long> bootcampIds);

    Mono<Void> deleteBootcamp(Long bootcampId);
}
