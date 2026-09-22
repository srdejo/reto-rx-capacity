package com.pragma.capacity.application.dto.response;

import java.util.List;

public record BootcampCapacitiesResponseDto(
        Long bootcampId,
        List<CapacityResponseDto> capacities
) {
}
