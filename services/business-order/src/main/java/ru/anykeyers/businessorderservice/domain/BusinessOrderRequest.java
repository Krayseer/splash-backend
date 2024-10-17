package ru.anykeyers.businessorderservice.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Данные о бизнес заказе
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessOrderRequest {

    /**
     * Идентификатор заказа
     */
    @NotNull
    private Long orderId;
    /**
     * Имя пользователя работника
     */
    @NotNull
    private UUID employeeId;

}
