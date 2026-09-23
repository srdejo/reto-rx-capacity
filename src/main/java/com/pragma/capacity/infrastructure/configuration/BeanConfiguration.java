package com.pragma.capacity.infrastructure.configuration;

import com.pragma.capacity.domain.api.IBootcampCapacityServicePort;
import com.pragma.capacity.domain.api.ICapacityServicePort;
import com.pragma.capacity.domain.spi.IBootcampCapacityPersistencePort;
import com.pragma.capacity.domain.spi.ICapacityPersistencePort;
import com.pragma.capacity.domain.spi.ITechnologyClientPort;
import com.pragma.capacity.domain.usecase.BootcampCapacityUseCase;
import com.pragma.capacity.domain.usecase.CapacityUseCase;
import com.pragma.capacity.infrastructure.out.r2dbc.adapter.BootcampCapacityAdapter;
import com.pragma.capacity.infrastructure.out.r2dbc.adapter.CapacityAdapter;
import com.pragma.capacity.infrastructure.out.r2dbc.mapper.ICapacityEntityMapper;
import com.pragma.capacity.infrastructure.out.r2dbc.repository.IBootcampCapacityRepository;
import com.pragma.capacity.infrastructure.out.r2dbc.repository.ICapacityRepository;
import com.pragma.capacity.infrastructure.out.webclient.adapter.TechnologyWebClientAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final ICapacityRepository capacityRepository;
    private final ICapacityEntityMapper capacityEntityMapper;
    private final IBootcampCapacityRepository bootcampCapacityRepository;

    @Value("${webclient.technology}")
    private String technologyUrl;

    @Bean
    public ICapacityPersistencePort capacityPersistencePort() {
        return new CapacityAdapter(capacityRepository, capacityEntityMapper);
    }

    @Bean
    public IBootcampCapacityPersistencePort bootcampCapacityPersistencePort() {
        return new BootcampCapacityAdapter(bootcampCapacityRepository);
    }

    @Bean
    public ICapacityServicePort capacityServicePort() {
        return new CapacityUseCase(capacityPersistencePort(), technologyClientPort());
    }

    @Bean
    public IBootcampCapacityServicePort  bootcampCapacityServicePort() {
        return new BootcampCapacityUseCase(capacityPersistencePort(), technologyClientPort(),  bootcampCapacityPersistencePort());
    }

    @Bean
    public ITechnologyClientPort technologyClientPort() {
        return new TechnologyWebClientAdapter(WebClient.builder(), technologyUrl);
    }
}
