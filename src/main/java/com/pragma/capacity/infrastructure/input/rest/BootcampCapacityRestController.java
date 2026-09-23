package com.pragma.capacity.infrastructure.input.rest;

import com.pragma.capacity.application.dto.request.BootcampCapacitiesRequestDto;
import com.pragma.capacity.application.dto.response.BootcampCapacitiesResponseDto;
import com.pragma.capacity.application.handler.IBootcampCapacityHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bootcamp-capacities")
@RequiredArgsConstructor
public class BootcampCapacityRestController {

    private final IBootcampCapacityHandler bootcampCapacityHandler;

    @Operation(summary = "Associate a list of capacities with a bootcamp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Capacities associated with the bootcamp", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "One or more capacities do not exist", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public Mono<ResponseEntity<Void>> saveBootcampCapacities(@Valid @RequestBody BootcampCapacitiesRequestDto bootcampCapacitiesRequestDto) {
        return bootcampCapacityHandler.saveBootcampCapacities(bootcampCapacitiesRequestDto)
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
    }

    @Operation(summary = "Get the capacities associated with each requested bootcamp id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Capacities grouped by bootcamp returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BootcampCapacitiesResponseDto.class))))
    })
    @GetMapping()
    public Flux<BootcampCapacitiesResponseDto> getCapacitiesByBootcampIds(@RequestParam List<Long> bootcampIds) {
        return bootcampCapacityHandler.getCapacitiesByBootcampIds(bootcampIds);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bootcampId}")
    public Mono<Void> deleteBootcampCapacities(@PathVariable Long bootcampId) {
        return bootcampCapacityHandler.deleteBootcamp(bootcampId);
    }
}
