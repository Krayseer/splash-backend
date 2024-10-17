//package ru.anykeyers.notificationservice.service.impl;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.anykeyers.commonsapi.domain.user.UserDTO;
//import ru.anykeyers.notificationservice.repository.NotificationRepository;
//import ru.anykeyers.notificationservice.domain.Notification;
//import ru.anykeyers.notificationservice.domain.PushNotification;
//
///**
// * Тесты для {@link PushNotificationService}
// */
//@ExtendWith(MockitoExtension.class)
//class PushNotificationServiceTest {
//
//    @Mock
//    private NotificationRepository notificationRepository;
//
//    @InjectMocks
//    private PushNotificationService pushNotificationService;
//
//    @Captor
//    private ArgumentCaptor<PushNotification> notificationCaptor;
//
//    /**
//     * Тест отправки push уведомления
//     */
//    @Test
//    void testSendNotification() {
//        Notification notification = new Notification("subject", "body");
//        UserDTO user = UserDTO.builder().username("test-user").build();
//        pushNotificationService.sendNotification(user, notification);
//        Mockito.verify(notificationRepository).save(notificationCaptor.capture());
//        PushNotification pushNotification = notificationCaptor.getValue();
//        Assertions.assertEquals("test-user", pushNotification.getUsername());
//        Assertions.assertEquals("subject", pushNotification.getSubject());
//        Assertions.assertEquals("body", pushNotification.getMessage());
//    }
//
//    /**
//     * Тест удаления push уведомления
//     */
//    @Test
//    void deleteNotification() {
//        pushNotificationService.deleteNotification(1L);
//        Mockito.verify(notificationRepository).deleteById(1L);
//    }
//}