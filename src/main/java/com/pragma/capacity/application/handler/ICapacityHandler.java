package com.pragma.capacity.application.handler;

import com.pragma.capacity.application.dto.request.CapacityRequestDto;
import com.pragma.capacity.application.dto.response.CapacityResponseDto;
import com.pragma.capacity.application.dto.response.PagedResponseDto;
import reactor.core.publisher.Mono;

public interface ICapacityHandler {

    Mono<Void> saveCapacity(CapacityRequestDto capacityRequestDto);

    Mono<PagedResponseDto<CapacityResponseDto>> getAllCapacities(int page, int size, String sortBy, String direction);
}
