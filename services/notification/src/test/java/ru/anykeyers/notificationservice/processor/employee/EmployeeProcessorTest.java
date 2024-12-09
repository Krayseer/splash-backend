package ru.anykeyers.notificationservice.processor.employee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.domain.configuration.employee.EmployeeDTO;
import ru.anykeyers.commonsapi.domain.configuration.employee.EmployeeStatus;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.remote.RemoteUserService;
import ru.anykeyers.notificationservice.domain.Notification;
import ru.anykeyers.notificationservice.service.NotificationServiceCompound;

import java.util.UUID;

/**
 * Тесты для {@link EmployeeProcessor}
 */
@ExtendWith(MockitoExtension.class)
class EmployeeProcessorTest {

    @Mock
    private RemoteUserService remoteUserService;

    @Mock
    private EmployeeNotificationCreator creator;

    @Mock
    private NotificationServiceCompound serviceCompound;

    @InjectMocks
    private EmployeeProcessor processor;

    private final User employeeUser = User.builder().id(UUID.randomUUID()).build();

    private final UUID holderId = UUID.randomUUID();

    private final User holderUser = User.builder().id(holderId).build();

    private final ConfigurationDTO configuration = ConfigurationDTO.builder().id(1L).userId(holderId).build();

    /**
     * Тест отправки уведомления о принятии работником приглашения на работу
     */
    @Test
    void processEmployeeInvitationApply() {
        // Подготовка
        EmployeeDTO employeeDTO = new EmployeeDTO(employeeUser, EmployeeStatus.ACTIVE, configuration);

        Notification employeeNotification = new Notification();
        Mockito.when(creator.createEmployeeInvitationNotification(employeeDTO)).thenReturn(employeeNotification);

        Mockito.when(remoteUserService.getUser(holderId)).thenReturn(holderUser);
        Notification holderNotification = new Notification();
        Mockito.when(creator.createCarWashOwnerNotificationEmployeeApplyInvitation(employeeDTO)).thenReturn(holderNotification);

        // Действие
        processor.processEmployeeInvitationApply(employeeDTO);

        // Проверка
        Mockito.verify(serviceCompound).sendNotification(holderUser, holderNotification);
        Mockito.verify(serviceCompound).sendNotification(employeeUser, employeeNotification);
    }

    /**
     * Тест отправки уведомления пользователю о том, что ему на заказ назначен работник
     */
    @Test
    void processOrderEmployeeApply() {
        // Подготовка
        OrderDTO orderDTO = OrderDTO.builder().user(holderUser).build();
        Notification notification = new Notification();
        Mockito.when(creator.createNotificationEmployeeOrderApply(orderDTO)).thenReturn(notification);

        // Действие
        processor.processOrderEmployeeApply(orderDTO);

        // Проверка
        Mockito.verify(serviceCompound).sendNotification(holderUser, notification);
    }

}