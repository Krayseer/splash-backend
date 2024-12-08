package ru.anykeyers.notificationservice.processor.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;
import ru.anykeyers.notificationservice.service.NotificationServiceCompound;

/**
 * Сервис отправки уведомлений для заказов
 */
@Service
@RequiredArgsConstructor
class OrderProcessor {

    private final RemoteConfigurationService remoteConfigurationService;

    private final OrderNotificationCreator orderNotificationCreator;

    private final NotificationServiceCompound notificationServiceCompound;

    /**
     * Уведомление, что был создан новый заказ
     */
    public void processOrderCreate(OrderDTO order) {
        notificationServiceCompound.sendNotification(
                order.getUser(),
                orderNotificationCreator.createOrderCreateMessage(order)
        );
    }

    /**
     * Уведомление, что заказ был удален
     *
     * @param order данные о заказе
     */
    public void processOrderDelete(OrderDTO order) {
        notificationServiceCompound.sendNotification(
                order.getUser(),
                orderNotificationCreator.createOrderDeleteMessage(order)
        );
    }

}
