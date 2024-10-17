package ru.anykeyers.statistics.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@DiscriminatorColumn(name="descriminatorColumn")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CarWashMetric {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор автомойки
     */
    private Long carWashId;

}
