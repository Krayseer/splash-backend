package ru.anykeyers.statistics.domain;

import jakarta.persistence.*;
import lombok.*;
import ru.anykeyers.statistics.domain.key.CarWashMetricId;

import java.util.Map;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CAR_WASH_METRIC")
public class CarWashMetric {

    @EmbeddedId
    private CarWashMetricId id;

    @Column(name = "ORDERS_COUNT")
    private Long ordersCount;

    @ElementCollection
    @CollectionTable(
            name = "CAR_WASH_SERVICE_COUNT",
            joinColumns = {
                    @JoinColumn(name = "CAR_WASH_ID", referencedColumnName = "CAR_WASH_ID"),
                    @JoinColumn(name = "DATE", referencedColumnName = "DATE")
            }
    )
    @MapKeyColumn(name = "SERVICE_ID")
    @Column(name = "SERVICE_COUNT")
    private Map<Long, Long> countByServiceId;

}
