package ru.anykeyers.notificationservice.service;

import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.notificationservice.domain.Notification;

/**
 * Сервис отправки уведомлений
 */
public interface NotificationService {

    /**
     * Отправить уведомление
     *
     * @param user          пользователь, которому нужно отправить уведомление
     * @param notification  уведомление
     */
    void sendNotification(User user, Notification notification);

    /**
     * Поддерживается ли отправка уведомлений
     *
     * @param userSetting настройки пользователя
     */
    boolean supports(User.Setting userSetting);

}
