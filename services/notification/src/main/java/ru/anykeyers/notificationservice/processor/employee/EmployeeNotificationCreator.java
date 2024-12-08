package ru.anykeyers.notificationservice.processor.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.configuration.employee.EmployeeDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;
import ru.anykeyers.commonsapi.remote.RemoteUserService;
import ru.anykeyers.notificationservice.Messages;
import ru.anykeyers.notificationservice.domain.Notification;

/**
 * Генератор уведомлений для работников
 */
@Service
@RequiredArgsConstructor
class EmployeeNotificationCreator {

    private final Messages messages;

    private final RemoteConfigurationService remoteConfigurationService;

    /**
     * Создать уведомление работнику об успешном принятии на работу
     */
    public Notification createEmployeeInvitationNotification(EmployeeDTO employee) {
        ConfigurationDTO configuration = employee.getConfiguration();
        return new Notification(
                messages.getMessage("employee.invitation.apply.subject"),
                messages.getMessage("employee.invitation.apply", configuration.getOrganizationInfo().getName(), configuration.getAddress().getAddress())
        );
    }

    /**
     * Создать уведомление хозяину автомойки о новом работнике
     */
    public Notification createCarWashOwnerNotificationEmployeeApplyInvitation(EmployeeDTO employee) {
        User employeeAsUser = employee.getUser();
        return new Notification(
                messages.getMessage("car-wash.owner.invitation.apply.subject"),
                messages.getMessage("car-wash.owner.invitation.apply",
                        employeeAsUser.getUserInfo().getFullName(),
                        employeeAsUser.getUsername(),
                        employeeAsUser.getRoles())
        );
    }

    /**
     * Создать уведомление о назначении работника на заказ
     */
    public Notification createNotificationEmployeeOrderApply(OrderDTO order) {
        ConfigurationDTO configuration = remoteConfigurationService.getConfiguration(order.getCarWashId());
        return new Notification(
                messages.getMessage("order.employee.apply.subject"),
                messages.getMessage("order.employee.apply", configuration.getOrganizationInfo().getName(), configuration.getAddress().getAddress())
        );
    }

}
