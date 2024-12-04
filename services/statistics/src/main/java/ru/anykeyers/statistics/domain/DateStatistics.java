package ru.anykeyers.statistics.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateStatistics {

    private String date;

    private long ordersCount;

    private long serviceCountSummary;

    private long servicePriceSummary;

}
