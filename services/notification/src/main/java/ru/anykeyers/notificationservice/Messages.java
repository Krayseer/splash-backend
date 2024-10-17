package ru.anykeyers.notificationservice;

import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

/**
 * Сообщения приложения
 */
@Component
public class Messages {

    private final ResourceBundle messages = ResourceBundle.getBundle("messages");

    /**
     * Получить сообщение
     *
     * @param key ключ сообщения
     */
    public String getMessage(String key) {
        return messages.getString(key);
    }

    /**
     * Получить сообщение
     *
     * @param key   ключ сообщения
     * @param args  аргументы
     */
    public String getMessage(String key, Object... args) {
        return String.format(getMessage(key), args);
    }

}
