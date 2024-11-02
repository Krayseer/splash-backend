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
    private String longitude;
    /**
     * Широта
     */
    private String latitude;
    /**
     * Название улицы
     */
    private String address;
}
