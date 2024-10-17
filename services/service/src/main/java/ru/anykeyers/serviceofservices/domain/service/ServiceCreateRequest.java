package ru.anykeyers.serviceofservices.domain.service;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Запрос с данными об услуге
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCreateRequest {

    /**
     * Идентификатор автомойки
     */
    @NotNull
    private Long carWashId;
    /**
     * Название услуги
     */
    @NotNull
    private String name;
    /**
     * Время выполнения услуги
     */
    @NotNull
    private Long duration;
    /**
     * Цена
     */
    @NotNull
    private Integer price;

}
