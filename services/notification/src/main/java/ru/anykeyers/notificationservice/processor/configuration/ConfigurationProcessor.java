package ru.anykeyers.notificationservice.processor.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.remote.RemoteUserService;
import ru.anykeyers.notificationservice.service.NotificationServiceCompound;

/**
 * Сервис отправки уведомлений для автомоек
 */
@Service
@RequiredArgsConstructor
public class ConfigurationProcessor {

    private final RemoteUserService remoteUserService;

    private final ConfigurationNotificationCreator notificationCreator;

    private final NotificationServiceCompound notificationServiceCompound;

    /**
     * Уведомление, что был проверен ИНН
     *
     * @param configuration конфигурация автомоек
     */
    public void processConfigurationValidate(ConfigurationDTO configuration) {
        User user = remoteUserService.getUser(configuration.getUserId());
        notificationServiceCompound.sendNotification(
                user, notificationCreator.createConfigurationValidateMessage(configuration)
        );
    }

}
