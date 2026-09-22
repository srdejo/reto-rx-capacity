package com.pragma.capacity.application.dto.request;

import java.util.List;

public record BootcampCapacitiesRequestDto(
        Long bootcampId,
        List<Long> capacityIds
) {
}
