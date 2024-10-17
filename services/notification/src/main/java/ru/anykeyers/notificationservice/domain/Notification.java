package ru.anykeyers.notificationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Уведомление
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    /**
     * Тема
     */
    private String subject;
    /**
     * Сообщение
     */
    private String message;

}
