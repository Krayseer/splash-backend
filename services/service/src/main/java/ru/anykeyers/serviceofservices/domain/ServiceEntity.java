package ru.anykeyers.serviceofservices.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Услуга
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SERVICE")
public class ServiceEntity {

    /**
     * Идентификатор
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор исходной услуги (общий для всех её версий)
     */
    @Column(name = "ORIGINAL_SERVICE_ID")
    private Long originalServiceId;

    /**
     * Идентификатор автомойки
     */
    @Column(name = "CAR_WASH_ID")
    private Long carWashId;

    /**
     * Название
     */
    @Column(name = "NAME")
    private String name;

    /**
     * Продолжительность выполнения в миллисекундах
     */
    @Column(name = "DURATION")
    private long duration;

    /**
     * Цена
     */
    @Column(name = "PRICE")
    private int price;

    /**
     * Версия услуги
     */
    @Column(name = "VERSION")
    private String version;

    /**
     * Актуальна ли версия
     */
    @Column(name = "IS_CURRENT")
    private boolean actual;

}
