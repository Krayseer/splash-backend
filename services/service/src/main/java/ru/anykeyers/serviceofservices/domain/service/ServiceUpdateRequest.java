package ru.anykeyers.serviceofservices.domain.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUpdateRequest {

    /**
     * Идентификатор услуги
     */
    @NotNull
    private Long id;
    /**
     * Название
     */
    @NotNull
    private String name;
    /**
     * Продолжительность
     */
    @NotNull
    private Long duration;
    /**
     * Цена
     */
    @NotNull
    private Integer price;

}
