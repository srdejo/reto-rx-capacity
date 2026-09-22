package com.pragma.capacity.domain.model;

import java.util.List;

public record CapacityTechnologies(
        Long capacityId,
        List<TechnologyModel> technologies
) {
}
