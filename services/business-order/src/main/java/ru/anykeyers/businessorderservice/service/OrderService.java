package ru.anykeyers.businessorderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.anykeyers.businessorderservice.repository.BusinessOrderRepository;
import ru.anykeyers.businessorderservice.domain.BusinessOrder;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.utils.DateUtils;
import ru.anykeyers.commonsapi.MessageQueue;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;
import ru.anykeyers.commonsapi.remote.RemoteOrderService;
import ru.anykeyers.commonsapi.remote.RemoteUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final RemoteUserService remoteUserService;

    private final RemoteOrderService remoteOrderService;

    private final RemoteConfigurationService remoteConfigurationService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final BusinessOrderRepository businessOrderRepository;

    /**
     * Получить список всех заказов работника
     */
    public List<OrderDTO> getOrders(String username) {
        List<BusinessOrder> businessOrders = businessOrderRepository.findByEmployeeId(UUID.randomUUID());//TODO доделать
        return remoteOrderService.getOrders(businessOrders.stream().map(BusinessOrder::getOrderId).toList());
    }

    /**
     * Получить свободных работников на заказ
     *
     * @param orderId идентификатор заказа
     */
    public List<User> getFreeEmployees(Long orderId) {
        OrderDTO order = remoteOrderService.getOrder(orderId);
        return new ArrayList<>(remoteUserService.getUsers(getFreeEmployees(order))); //TODO: ПОПРАВИТЬ НА SET
    }

    /**
     * Назначить работника на заказ
     *
     * @param orderId       идентификатор заказа
     * @param employeeId    идентификатор работника
     */
    public void appointOrderEmployee(Long orderId, UUID employeeId) {
        BusinessOrder businessOrder = BusinessOrder.builder()
                .orderId(orderId)
                .employeeId(employeeId)
                .build();
        businessOrderRepository.save(businessOrder);
        log.info("Appoint order employee '{}' for order '{}'", employeeId, orderId);
    }

    /**
     * Удалить работника с заказа
     *
     * @param businessOrderId идентификатор заказа
     */
    public void disappointEmployeeFromOrder(Long businessOrderId) {
        BusinessOrder order = businessOrderRepository.findById(businessOrderId).orElseThrow(
                () -> new RuntimeException("Order not found")
        );
        kafkaTemplate.send(MessageQueue.ORDER_EMPLOYEE_DISAPPOINT, order.getOrderId());
        businessOrderRepository.deleteById(businessOrderId);
    }

    /**
     * Получить свободных работников на заказ
     *
     * @param order заказ
     */
    public Set<UUID> getFreeEmployees(OrderDTO order) {
        Set<UUID> employees = remoteConfigurationService.getEmployees(order.getCarWashId()).stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        List<OrderDTO> orders = remoteOrderService.getOrders(order.getCarWashId(), DateUtils.toDate(order.getStartTime()));
        if (CollectionUtils.isEmpty(orders)) {
            return employees;
        }
        getBusyEmployees(orders, order).forEach(employees::remove);
        return employees;
    }

    private List<UUID> getBusyEmployees(List<OrderDTO> savedOrders, OrderDTO newOrder) {
        return savedOrders.stream()
                .filter(savedOrder -> isOverlapOrders(savedOrder, newOrder))
                .map(OrderDTO::getId)
                .map(businessOrderRepository::findByOrderId)
                .map(BusinessOrder::getEmployeeId)
                .toList();
    }

    /**
     * Пересекаются ли заказы
     *
     * @param savedOrder    сохраненный заказ
     * @param currentOrder  текущий заказ
     */
    private boolean isOverlapOrders(OrderDTO savedOrder, OrderDTO currentOrder) {
        long savedStart = savedOrder.getStartTime();
        long currentStart = currentOrder.getStartTime();
        long savedEnd = savedOrder.getEndTime();
        long currentEnd = currentOrder.getEndTime();
        return currentStart <= savedEnd && currentEnd >= savedStart && currentEnd <= savedEnd ||
               currentStart >= savedStart && currentEnd <= savedEnd ||
                currentEnd >= savedEnd && currentStart >= savedStart && currentStart <= savedEnd;
    }

}
