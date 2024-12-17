package ru.anykeyers.notificationservice.processor.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;
import ru.anykeyers.notificationservice.Messages;
import ru.anykeyers.notificationservice.domain.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Генератор уведомлений о заказах
 */
@Component
@RequiredArgsConstructor
public class OrderNotificationCreator {

    private final Messages messages;

    private final RemoteConfigurationService remoteConfigurationService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    /**
     * Создать уведомление о создании заказа
     *
     * @param order заказ
     */
    public Notification createOrderCreateMessage(OrderDTO order) {
        ConfigurationDTO configurationDTO = remoteConfigurationService.getConfiguration(order.getCarWashId());
        return new Notification(
                messages.getMessage("order.create.apply.subject"),
                messages.getMessage("order.create.apply",
                        configurationDTO.getOrganizationInfo().getName(),
                        order.getBox().getName(),
                        dateFormat.format(new Date(order.getStartTime())))
        );
    }

    /**
     * Создать уведомление об удалении заказа
     *
     * @param order заказ
     */
    public Notification createOrderDeleteMessage(OrderDTO order) {
        ConfigurationDTO configurationDTO = remoteConfigurationService.getConfiguration(order.getCarWashId());
        return new Notification(
                messages.getMessage("order.delete.apply.subject"),
                messages.getMessage("order.delete.apply",
                        configurationDTO.getOrganizationInfo().getName(),
                        order.getStartTime())
        );
    }

}
