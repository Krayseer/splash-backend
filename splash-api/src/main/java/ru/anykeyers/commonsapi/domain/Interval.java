package ru.anykeyers.commonsapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Отрезок времени
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interval {

    /**
     * Время начала
     */
    private long start;

    /**
     * Время конца
     */
    private long end;

    public boolean isInRange(long time) {
        return time >= start && time <= end;
    }

    public static Interval of(long start, long end) {
        return new Interval(start, end);
    }

}
