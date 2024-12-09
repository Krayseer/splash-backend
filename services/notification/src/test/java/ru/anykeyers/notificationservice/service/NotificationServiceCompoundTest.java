package ru.anykeyers.notificationservice.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.notificationservice.domain.Notification;
import ru.anykeyers.notificationservice.service.impl.PushNotificationService;
import ru.anykeyers.notificationservice.service.impl.SmtpEmailService;

import java.util.List;

/**
 * Тесты для {@link NotificationServiceCompound}
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceCompoundTest {

    @Mock
    private SmtpEmailService smtpEmailService;

    @Mock
    private PushNotificationService pushNotificationService;

    @InjectMocks
    private NotificationServiceCompound notificationServiceCompound;

    @BeforeEach
    void setUp() {
        notificationServiceCompound = new NotificationServiceCompound(
                List.of(pushNotificationService, smtpEmailService)
        );
    }

    /**
     * Тест отправки уведомления в зависимости от настроек пользователя
     */
    @Test
    @SneakyThrows
    void sendNotification() {
        // Подготовка
        User.Setting setting = new User.Setting(true, false);
        User user = User.builder().setting(setting).build();
        Notification notification = new Notification();

        Mockito.when(pushNotificationService.supports(setting)).thenReturn(true);
        Mockito.when(smtpEmailService.supports(setting)).thenReturn(false);

        // Действие
        notificationServiceCompound.sendNotification(user, notification);
        Thread.sleep(200);

        // Проверка
        Mockito.verify(pushNotificationService).sendNotification(user, notification);
        Mockito.verify(smtpEmailService, Mockito.never()).sendNotification(user, notification);
    }

}