package ru.anykeyers.notificationservice.processor.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.user.EmployeeDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.remote.RemoteUserService;
import ru.anykeyers.notificationservice.service.NotificationServiceCompound;

/**
 * Обработчик сообщений по работникам
 */
@Service
@RequiredArgsConstructor
class EmployeeProcessor {

    private final RemoteUserService remoteUserService;

    private final EmployeeNotificationCreator employeeNotificationCreator;

    private final NotificationServiceCompound notificationServiceCompound;

    /**
     * Создать уведомление о принятии работником пришглашения на работу
     *
     * @param employee работник
     */
    public void processEmployeeInvitationApply(EmployeeDTO employee) {
        notificationServiceCompound.sendNotification(
                remoteUserService.getUser(employee.getUserId()),
                employeeNotificationCreator.createEmployeeInvitationNotification(employee)
        );
        notificationServiceCompound.sendNotification(
                remoteUserService.getUser(employee.getConfiguration().getUserId()),
                employeeNotificationCreator.createCarWashOwnerNotificationEmployeeApplyInvitation(employee)
        );
    }

    /**
     * Обработать назначение работника заказу
     *
     * @param order заказ
     */
    public void processOrderEmployeeApply(OrderDTO order) {
        User user = remoteUserService.getUser(order.getUserId());
        notificationServiceCompound.sendNotification(
                user, employeeNotificationCreator.createNotificationEmployeeOrderApply(order)
        );
    }

}
