package ru.anykeyers.commonsapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Адрес
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    /**
     * Долгота
     */
    String longitude;
    /**
     * Широта
     */
    String latitude;
    /**
     * Название улицы
     */
    String address;
}
