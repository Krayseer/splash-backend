package ru.anykeyers.notificationservice.service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.notificationservice.domain.Notification;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Комплекс сервисов отправки уведомлений
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceCompound {

    private final List<NotificationService> notificationServices;

    private final ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Отправить уведомление пользователю
     *
     * @param user          пользователь
     * @param notification  уведомление
     */
    public void sendNotification(User user, Notification notification) {
        for (NotificationService service : getNotificationServices(user.getSetting())) {
            threadPool.execute(() -> service.sendNotification(user, notification));
        }
    }

    private List<NotificationService> getNotificationServices(User.Setting userSetting) {
        return notificationServices.stream()
                .filter(service -> service.supports(userSetting))
                .toList();
    }

    @PreDestroy
    public void shutdown() {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
