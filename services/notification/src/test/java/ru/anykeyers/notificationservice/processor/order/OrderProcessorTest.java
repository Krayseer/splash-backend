package ru.anykeyers.notificationservice.processor.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.notificationservice.domain.Notification;
import ru.anykeyers.notificationservice.service.NotificationServiceCompound;

import java.util.UUID;

/**
 * Тесты для {@link OrderProcessor}
 */
@ExtendWith(MockitoExtension.class)
class OrderProcessorTest {

    @Mock
    private OrderNotificationCreator creator;

    @Mock
    private NotificationServiceCompound serviceCompound;

    @InjectMocks
    private OrderProcessor processor;

    private final User user = User.builder().id(UUID.randomUUID()).build();

    /**
     * Тест обработки создания уведомления о создании заказа
     */
    @Test
    void processOrderCreate() {
        // Подготовка
        OrderDTO orderDTO = OrderDTO.builder().user(user).build();
        Notification notification = new Notification();
        Mockito.when(creator.createOrderCreateMessage(orderDTO)).thenReturn(notification);

        // Действие
        processor.processOrderCreate(orderDTO);

        // Проверка
        Mockito.verify(serviceCompound).sendNotification(user, notification);
    }

    /**
     * Тест обработки создания уведомления об удалении заказа
     */
    @Test
    void processOrderDelete() {
        // Подготовка
        OrderDTO orderDTO = OrderDTO.builder().user(user).build();
        Notification notification = new Notification();
        Mockito.when(creator.createOrderDeleteMessage(orderDTO)).thenReturn(notification);

        // Действие
        processor.processOrderDelete(orderDTO);

        // Проверка
        Mockito.verify(serviceCompound).sendNotification(user, notification);
    }

}