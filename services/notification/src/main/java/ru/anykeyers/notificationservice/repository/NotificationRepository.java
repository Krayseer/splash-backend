package ru.anykeyers.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anykeyers.notificationservice.domain.PushNotification;

import java.util.List;

/**
 * DAO для работы с push-уведомлениями
 */
@Repository
public interface NotificationRepository extends JpaRepository<PushNotification, Long> {

    /**
     * Получить список push-уведомлений пользователя
     *
     * @param username имя пользователя
     */
    List<PushNotification> findByUsername(String username);

}
