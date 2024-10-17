package ru.anykeyers.notificationservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.domain.user.UserSettingDTO;
import ru.anykeyers.notificationservice.domain.Notification;
import ru.anykeyers.notificationservice.repository.NotificationRepository;
import ru.anykeyers.notificationservice.domain.PushNotification;
import ru.anykeyers.notificationservice.domain.PushNotificationDTO;
import ru.anykeyers.notificationservice.service.NotificationService;

import java.time.Instant;
import java.util.List;

/**
 * Сервис обработки push-уведомлений
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationService implements NotificationService {

    private final ModelMapper modelMapper;

    private final NotificationRepository notificationRepository;

    /**
     * Получить список push-уведомлений
     *
     * @param user пользователь
     */
    public List<PushNotificationDTO> getNotifications(User user) {
        return notificationRepository.findByUsername(user.getUsername()).stream()
                .map(notification -> modelMapper.map(notification, PushNotificationDTO.class))
                .toList();
    }

    @Override
    public void sendNotification(User user, Notification notification) {
        PushNotification pushNotification = PushNotification.builder()
                .username(user.getUsername())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .createdAt(Instant.now())
                .build();
        notificationRepository.save(pushNotification);
        log.info("Created notification: {}", pushNotification);
    }

    @Override
    public boolean supports(UserSettingDTO userSetting) {
        return userSetting.pushEnabled();
    }

    /**
     * Удалить push уведомление
     *
     * @param pushId идентификатор уведомления
     */
    public void deleteNotification(long pushId) {
        notificationRepository.deleteById(pushId);
        log.info("Deleted notification: {}", pushId);
    }

}
