package ru.anykeyers.orderservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.anykeyers.commonsapi.domain.PaymentType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    private Long carWashId;

    private List<Long> serviceIds;

    private PaymentType paymentType;

    private Long startTime;

}
