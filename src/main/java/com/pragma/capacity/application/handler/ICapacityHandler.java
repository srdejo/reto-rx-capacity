package com.pragma.capacity.application.handler;

import com.pragma.capacity.application.dto.request.CapacityRequestDto;
import com.pragma.capacity.application.dto.response.CapacityResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapacityHandler {

    Mono<Void> saveCapacity(CapacityRequestDto capacityRequestDto);

    Flux<CapacityResponseDto> getAllCapacities();
}
