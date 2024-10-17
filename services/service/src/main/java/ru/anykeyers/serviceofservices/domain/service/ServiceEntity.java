package ru.anykeyers.serviceofservices.domain.service;

import jakarta.persistence.*;
import lombok.*;

/**
 * Услуга
 */
@Getter
@Setter
@Entity
@Builder
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

}
