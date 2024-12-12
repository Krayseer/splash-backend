package ru.anykeyers.notificationservice.processor.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.notificationservice.Messages;
import ru.anykeyers.notificationservice.domain.Notification;

/**
 * Генератор уведомлений для автомоек
 */
@Component
@RequiredArgsConstructor
public class ConfigurationNotificationCreator {

    private final Messages messages;

    /**
     * Создать уведомление о проверке ИНН
     *
     * @param configuration конфигурация автомойки
     */
    public Notification createConfigurationValidateMessage(ConfigurationDTO configuration) {
        String resultMessage = configuration.isValid()
                ? messages.getMessage("car-wash.validation.success")
                : messages.getMessage("car-wash.validation.invalid");
        return new Notification(
                messages.getMessage("car-wash.validation.subject"),
                resultMessage
        );
    }
}
