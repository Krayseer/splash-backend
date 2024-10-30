package ru.anykeyers.commonsapi.domain.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.anykeyers.commonsapi.domain.PaymentType;
import ru.anykeyers.commonsapi.domain.configuration.BoxDTO;
import ru.anykeyers.commonsapi.domain.service.ServiceDTO;
import ru.anykeyers.commonsapi.domain.user.User;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    private Long id;

    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    private UUID userId;

    private User user;
    private List<ServiceDTO> services;
    private BoxDTO box;

    private Long carWashId;

    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    private Long boxId;

    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    private OrderState orderState;

    private List<Long> serviceIds;

    private PaymentType paymentType;

    private long startTime;

//    @JsonProperty(
//            access = JsonProperty.Access.READ_ONLY
//    )
    private long endTime;

//    @JsonProperty(
//            access = JsonProperty.Access.READ_ONLY
//    )
    private String createdAt;

}
