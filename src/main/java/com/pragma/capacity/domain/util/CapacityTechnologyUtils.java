package com.pragma.capacity.domain.util;

import com.pragma.capacity.domain.model.CapacityModel;
import com.pragma.capacity.domain.model.CapacityTechnologies;
import com.pragma.capacity.domain.model.TechnologyModel;
import com.pragma.capacity.domain.spi.ITechnologyClientPort;
import reactor.core.publisher.Mono;

import java.util.List;

public final class CapacityTechnologyUtils {

    private CapacityTechnologyUtils() {
    }

    public static Mono<List<CapacityModel>> attachTechnologies(List<CapacityModel> capacities,
                                                               ITechnologyClientPort technologyClientPort) {
        List<Long> capacityIds = capacities.stream().map(CapacityModel::getId).toList();
        if (capacityIds.isEmpty()) {
            return Mono.just(capacities);
        }

        return technologyClientPort.getTechnologiesByCapacityIds(capacityIds)
                .collectMap(CapacityTechnologies::capacityId, CapacityTechnologies::technologies)
                .map(technologiesByCapacityId -> {
                    capacities.forEach(capacity -> {
                        List<TechnologyModel> technologies = technologiesByCapacityId.getOrDefault(capacity.getId(), List.of());
                        capacity.setTechnologies(technologies);
                    });
                    return capacities;
                });
    }
}
