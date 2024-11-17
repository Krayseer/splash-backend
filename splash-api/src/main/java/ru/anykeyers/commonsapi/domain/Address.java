package ru.anykeyers.commonsapi.domain;

import lombok.*;

/**
 * Адрес
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    /**
     * Долгота
     */
    private double longitude;
    /**
     * Широта
     */
    private double latitude;
    /**
     * Название улицы
     */
    private String address;
}
