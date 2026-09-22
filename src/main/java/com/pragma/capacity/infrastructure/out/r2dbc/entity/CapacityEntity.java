package com.pragma.capacity.infrastructure.out.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("capacity")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CapacityEntity {
    @Id
    @Column("capacity_id")
    private Long id;

    private String name;
}
