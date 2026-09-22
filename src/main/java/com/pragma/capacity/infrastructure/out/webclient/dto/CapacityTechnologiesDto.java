package com.pragma.capacity.infrastructure.out.webclient.dto;

import java.util.List;

public record CapacityTechnologiesDto(
        Long capacityId,
        List<Long> technologyIds
) {
}
