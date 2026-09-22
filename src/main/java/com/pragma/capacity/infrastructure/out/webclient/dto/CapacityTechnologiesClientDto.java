package com.pragma.capacity.infrastructure.out.webclient.dto;

import java.util.List;

public record CapacityTechnologiesClientDto(
        Long capacityId,
        List<TechnologyClientDto> technologies
) {
}
