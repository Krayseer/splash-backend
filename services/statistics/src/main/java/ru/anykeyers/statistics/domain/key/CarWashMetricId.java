package ru.anykeyers.statistics.domain.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CarWashMetricId implements Serializable {

    @Column(name = "CAR_WASH_ID", nullable = false)
    private Long carWashId;

    @Column(name = "DATE", nullable = false)
    private LocalDate date;

}
