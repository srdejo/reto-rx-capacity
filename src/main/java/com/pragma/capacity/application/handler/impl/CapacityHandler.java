package com.pragma.capacity.application.handler.impl;

import com.pragma.capacity.application.dto.request.CapacityRequestDto;
import com.pragma.capacity.application.dto.response.CapacityResponseDto;
import com.pragma.capacity.application.handler.ICapacityHandler;
import com.pragma.capacity.application.mapper.ICapacityRequestMapper;
import com.pragma.capacity.application.mapper.ICapacityResponseMapper;
import com.pragma.capacity.domain.api.ICapacityServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<CapacityResponseDto> getAllCapacities() {
        return capacityServicePort.getAllCapacities().map(capacityResponseMapper::toResponse);
    }
}
