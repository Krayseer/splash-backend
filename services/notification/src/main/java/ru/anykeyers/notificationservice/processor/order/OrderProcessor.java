//package ru.anykeyers.notificationservice.processor.order;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
//import ru.anykeyers.commonsapi.domain.order.OrderDTO;
//import ru.anykeyers.commonsapi.domain.user.User;
//import ru.anykeyers.commonsapi.domain.user.UserDTO;
//import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;
//import ru.anykeyers.commonsapi.remote.RemoteUserService;
//import ru.anykeyers.notificationservice.domain.Notification;
//import ru.anykeyers.notificationservice.processor.MessageCreator;
//import ru.anykeyers.notificationservice.service.NotificationServiceCompound;
//
///**
// * Сервис отправки уведомлений для заказов
// */
//@Service
//@RequiredArgsConstructor
//class OrderProcessor {
//
//    private final RemoteUserService remoteUserService;
//
//    private final RemoteConfigurationService remoteConfigurationService;
//
//    private final NotificationServiceCompound notificationServiceCompound;
//
//    /**
//     * Уведомление, что был создан новый заказ
//     */
//    public void processOrderCreate(OrderDTO order) {
//        notifyUser(order, "Новый заказ", this::createOrderMessage);
//    }
//
//    /**
//     * Уведомление, что заказ был удален
//     *
//     * @param order данные о заказе
//     */
//    public void processOrderDelete(OrderDTO order) {
//        notifyUser(order, "Удаление заказа", this::deleteOrderMessage);
//    }
//
//    private void notifyUser(OrderDTO order, String notificationTitle, MessageCreator messageCreator) {
//        User user = remoteUserService.getUser(order.getUserId());
//        ConfigurationDTO configurationDTO = remoteConfigurationService.getConfiguration(order.getCarWashId());
//        String message = messageCreator.createMessage(configurationDTO, order);
//        notificationServiceCompound.sendNotification(user, new Notification(notificationTitle, message));
//    }
//
//    private String createOrderMessage(ConfigurationDTO configurationDTO, OrderDTO order) {
//        return String.format("""
//                Ваш заказ успешно принят.
//                Автомойка: %s
//                Бокс: %s
//                Время: %s
//                """, configurationDTO.getOrganizationInfo().getName(), order.getBoxId(), order.getStartTime());
//    }
//
//    private String employeeAssignmentMessage(ConfigurationDTO configurationDTO, OrderDTO order) {
//        return String.format("""
//                Вам назначен работник на заказ!
//                Ожидаем вас по адресу: %s.
//                Время: %s
//                """, configurationDTO.getAddress(), order.getStartTime());
//    }
//
//    private String deleteOrderMessage(ConfigurationDTO configuration, OrderDTO order) {
//        return String.format("""
//                Ваш заказ №%s был удален.
//                Автомойка: %s.
//                Время: %s
//                """, order.getId(), configuration.getAddress(), order.getStartTime());
//    }
//
//}
