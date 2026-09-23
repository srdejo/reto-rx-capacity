package com.pragma.capacity.infrastructure.out.webclient.adapter;

import com.pragma.capacity.domain.exception.TechnologyNotFoundException;
import com.pragma.capacity.domain.exception.TechnologyServiceUnavailableException;
import com.pragma.capacity.domain.model.CapacityTechnologies;
import com.pragma.capacity.domain.model.TechnologyModel;
import com.pragma.capacity.domain.spi.ITechnologyClientPort;
import com.pragma.capacity.infrastructure.out.webclient.dto.CapacityTechnologiesClientDto;
import com.pragma.capacity.infrastructure.out.webclient.dto.CapacityTechnologiesDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class TechnologyWebClientAdapter implements ITechnologyClientPort {

    private final WebClient.Builder webClientBuilder;
    private final String technologyUrl;

    public TechnologyWebClientAdapter(WebClient.Builder webClientBuilder, String technologyUrl) {
        this.webClientBuilder = webClientBuilder;
        this.technologyUrl = technologyUrl;
    }

    @Override
    public Mono<Void> associateTechnologies(Long capacityId, List<Long> technologyIds) {

        CapacityTechnologiesDto body = new CapacityTechnologiesDto(capacityId, technologyIds);

        return webClientBuilder
                .baseUrl(technologyUrl)
                .build()
                .post()
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.value() == 404, _ -> Mono.error(new TechnologyNotFoundException()))
                .onStatus(HttpStatusCode::is5xxServerError,
                        _ -> Mono.error(new TechnologyServiceUnavailableException()))
                .bodyToMono(Void.class)
                .onErrorMap(WebClientRequestException.class, _ -> new TechnologyServiceUnavailableException());
    }

    @Override
    public Flux<CapacityTechnologies> getTechnologiesByCapacityIds(List<Long> capacityIds) {
        return webClientBuilder
                .baseUrl(technologyUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("capacityIds", capacityIds)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, _ -> Mono.error(new TechnologyServiceUnavailableException()))
                .bodyToFlux(CapacityTechnologiesClientDto.class)
                .onErrorMap(WebClientRequestException.class, _ -> new TechnologyServiceUnavailableException())
                .map(dto -> new CapacityTechnologies(
                        dto.capacityId(),
                        dto.technologies().stream()
                                .map(technology -> new TechnologyModel(technology.id(), technology.name()))
                                .toList()));
    }

    @Override
    public Mono<Void> deleteTechnologiesByCapacityIds(List<Long> capacityIds) {
        if (capacityIds.isEmpty()) {
            return Mono.empty();
        }

        return webClientBuilder
                .baseUrl(technologyUrl)
                .build()
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("capacityIds", capacityIds)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError,
                        _ -> Mono.error(new TechnologyServiceUnavailableException()))
                .bodyToMono(Void.class)
                .onErrorMap(WebClientRequestException.class, _ -> new TechnologyServiceUnavailableException());
    }
}
