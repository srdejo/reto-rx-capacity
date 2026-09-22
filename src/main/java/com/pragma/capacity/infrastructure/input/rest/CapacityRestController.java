package com.pragma.capacity.infrastructure.input.rest;

import com.pragma.capacity.application.dto.request.BootcampCapacitiesRequestDto;
import com.pragma.capacity.application.dto.request.CapacityRequestDto;
import com.pragma.capacity.application.dto.response.BootcampCapacitiesResponseDto;
import com.pragma.capacity.application.dto.response.CapacityResponseDto;
import com.pragma.capacity.application.dto.response.PagedResponseDto;
import com.pragma.capacity.application.handler.ICapacityHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/capacity")
@RequiredArgsConstructor
public class CapacityRestController {

    private final ICapacityHandler capacityHandler;

    @Operation(summary = "Add a new capacity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Capacity created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    @PostMapping()
    public Mono<ResponseEntity<Void>> saveCapacity(@Valid @RequestBody CapacityRequestDto capacityRequestDto) {
        return capacityHandler.saveCapacity(capacityRequestDto)
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
    }

    @Operation(summary = "Get a paginated list of capacities, sorted by name or technology count")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of capacities returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagedResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination or sort parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping()
    public Mono<PagedResponseDto<CapacityResponseDto>> getAllCapacities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", schema = @Schema(allowableValues = {"name", "technologyCount"}, defaultValue = "name"))
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction", schema = @Schema(allowableValues = {"asc", "desc"}, defaultValue = "asc"))
            @RequestParam(defaultValue = "asc") String direction) {
        return capacityHandler.getAllCapacities(page, size, sortBy, direction);
    }

    @Operation(summary = "Associate a list of capacities with a bootcamp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Capacities associated with the bootcamp", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "One or more capacities do not exist", content = @Content)
    })
    @PostMapping("/bootcamp-capacities")
    public Mono<ResponseEntity<Void>> saveBootcampCapacities(@Valid @RequestBody BootcampCapacitiesRequestDto bootcampCapacitiesRequestDto) {
        return capacityHandler.saveBootcampCapacities(bootcampCapacitiesRequestDto)
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
    }

    @Operation(summary = "Get the capacities associated with each requested bootcamp id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Capacities grouped by bootcamp returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BootcampCapacitiesResponseDto.class))))
    })
    @GetMapping("/bootcamp-capacities")
    public Flux<BootcampCapacitiesResponseDto> getCapacitiesByBootcampIds(@RequestParam List<Long> bootcampIds) {
        return capacityHandler.getCapacitiesByBootcampIds(bootcampIds);
    }
}
