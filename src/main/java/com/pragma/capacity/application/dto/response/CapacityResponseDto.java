package com.pragma.capacity.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CapacityResponseDto {
    private Long id;
    private String name;
    private String description;
    private List<TechnologyResponseDto> technologies;
}
