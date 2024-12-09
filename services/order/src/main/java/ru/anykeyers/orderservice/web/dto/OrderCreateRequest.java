package ru.anykeyers.orderservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.anykeyers.commonsapi.domain.PaymentType;

import java.util.List;

/**
 * Данные для создания заказа
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    /**
     * Идентификатор автомойки
     */
    private Long carWashId;

    /**
     * Идентификаторы услуг
     */
    private List<Long> serviceIds;

    /**
     * Тип оплаты
     */
    private PaymentType paymentType;

    /**
     * Время начала заказа
     */
    private Long startTime;

}
