package ru.anykeyers.commonsapi.domain.order;

import lombok.*;
import ru.anykeyers.commonsapi.domain.PaymentType;
import ru.anykeyers.commonsapi.domain.configuration.BoxDTO;
import ru.anykeyers.commonsapi.domain.service.ServiceDTO;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;

    private UUID userId;

    private Long carWashId;

    private List<ServiceDTO> services;

    private BoxDTO box;

    private OrderState orderState;

    private PaymentType paymentType;

    private long startTime;

    private long endTime;

    private String createdAt;

}
