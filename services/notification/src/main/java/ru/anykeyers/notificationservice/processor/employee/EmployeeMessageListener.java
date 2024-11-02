package ru.anykeyers.notificationservice.processor.employee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.MessageQueue;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.user.EmployeeDTO;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeeMessageListener {

    private final EmployeeProcessor employeeProcessor;

    /**
     * Слушатель одобрения заявки работника
     */
    @KafkaListener(topics = MessageQueue.EMPLOYEE_INVITATION_APPLY)
    public void receiveApplyInvitation(EmployeeDTO employee) {
        employeeProcessor.processEmployeeInvitationApply(employee);
    }

    /**
     * Слушатель события назначения работника заказу
     */
    @KafkaListener(topics = MessageQueue.EMPLOYEE_ORDER_APPLY)
    public void receiveOrderApplyEmployee(OrderDTO order) {
        employeeProcessor.processOrderEmployeeApply(order);
    }

}
