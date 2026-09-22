package com.pragma.capacity.infrastructure.out.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("bootcamp_capacity")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BootcampCapacityEntity {

    @Id
    private Long id;

    @Column("capacity_id")
    private Long capacityId;

    @Column("bootcamp_id")
    private Long bootcampId;

    public BootcampCapacityEntity(Long capacityId, Long bootcampId) {
        this.capacityId = capacityId;
        this.bootcampId = bootcampId;
    }
}
