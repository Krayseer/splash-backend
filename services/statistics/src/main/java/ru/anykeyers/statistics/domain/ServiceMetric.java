package ru.anykeyers.statistics.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Метрики по услугам
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "SERVICE_METRIC")
public class ServiceMetric extends CarWashMetric {

    /**
     * Кол-во услуг
     */
    private long count;

    /**
     * Общая сумма выполненных услуг
     */
    private long sum;

    public ServiceMetric() {
        this.count = 0L;
        this.sum = 0L;
    }

    public void addCount(long count) {
        this.count += count;
    }

    public void addSum(long sum) {
        this.sum += sum;
    }

}
