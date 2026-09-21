package com.pragma.capacity.infrastructure.configuration;

import com.pragma.capacity.domain.api.ICapacityServicePort;
import com.pragma.capacity.domain.spi.ICapacityPersistencePort;
import com.pragma.capacity.domain.usecase.CapacityUseCase;
import com.pragma.capacity.infrastructure.out.r2dbc.adapter.CapacityAdapter;
import com.pragma.capacity.infrastructure.out.r2dbc.mapper.ICapacityEntityMapper;
import com.pragma.capacity.infrastructure.out.r2dbc.repository.ICapacityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final ICapacityRepository capacityRepository;
    private final ICapacityEntityMapper capacityEntityMapper;

    @Bean
    public ICapacityPersistencePort capacityPersistencePort() {
        return new CapacityAdapter(capacityRepository, capacityEntityMapper);
    }

    @Bean
    public ICapacityServicePort capacityServicePort() {
        return new CapacityUseCase(capacityPersistencePort());
    }
}
