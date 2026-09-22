package com.pragma.capacity.domain.model;

import java.util.List;

public record BootcampCapacitiesModel(
        Long bootcampId,
        List<CapacityModel> capacities
) {
}
