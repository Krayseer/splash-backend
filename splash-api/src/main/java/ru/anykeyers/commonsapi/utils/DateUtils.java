package ru.anykeyers.commonsapi.utils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Утилитарный класс для обработки времени
 */
public final class DateUtils {

    /**
     * Форматировать дату вида "dd-MM-yyyy" в {@link Instant}
     *
     * @param date дата в строковом формате
     */
    public static Instant toInstant(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
    }

    /**
     * Получить дату из полной информации о времени + дате в формате "день-месяц-год"
     *
     * @param instant время
     */
    public static String toDate(Instant instant) {
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        int day = localDate.getDayOfMonth();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        return String.format("%02d-%02d-%d", day, month, year);
    }

    /**
     * Получить дату из миллисекунд
     *
     * @param duration время в миллисекундах
     */
    public static String toDate(long duration) {
        Date date = new Date(duration);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(date);
    }

    /**
     * Добавить время вида "часы:минуты" к {@link Instant времени}
     *
     * @param instant   дата
     * @param time      время, которое нужно добавить
     */
    public static Instant addTimeToInstant(Instant instant, String time) {
        LocalTime localTime = LocalTime.parse(time);
        Duration duration = Duration.ofHours(localTime.getHour()).plusMinutes(localTime.getMinute());
        return instant.plus(duration);
    }

    /**
     * Добавить время вида "часы:минуты" к {@link Instant времени}
     *
     * @param instant   дата
     * @param time      время, которое нужно добавить
     */
    public static Instant addTimeToInstant(Instant instant, Instant time) {
        LocalTime localTime = LocalTime.parse(time.toString());
        Duration duration = Duration.ofHours(localTime.getHour()).plusMinutes(localTime.getMinute());
        return instant.plus(duration);
    }

}
