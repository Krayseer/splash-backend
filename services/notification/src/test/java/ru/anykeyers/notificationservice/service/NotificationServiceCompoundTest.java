//package ru.anykeyers.notificationservice.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.anykeyers.commonsapi.domain.user.UserDTO;
//import ru.anykeyers.commonsapi.domain.user.UserSettingDTO;
//import ru.anykeyers.notificationservice.domain.Notification;
//import ru.anykeyers.notificationservice.service.impl.PushNotificationService;
//import ru.anykeyers.notificationservice.service.impl.SmtpEmailService;
//
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//
///**
// * Тесты для {@link NotificationServiceCompound}
// */
//@ExtendWith(MockitoExtension.class)
//class NotificationServiceCompoundTest {
//
//    @Mock
//    private ExecutorService executorService;
//
//    @Mock
//    private PushNotificationService pushNotificationService;
//
//    @Mock
//    private SmtpEmailService smtpEmailService;
//
//    @InjectMocks
//    private NotificationServiceCompound notificationServiceCompound;
//
//    @Captor
//    private ArgumentCaptor<Runnable> runnableCaptor;
//
//    @BeforeEach
//    void setUp() {
//        notificationServiceCompound = new NotificationServiceCompound(
//                List.of(pushNotificationService, smtpEmailService)
//        );
//    }
//
//    /**
//     * Тест отправки уведомления в зависимости от настроек пользователя
//     */
//    @Test
//    void testSendNotification() {
//        UserSettingDTO setting = new UserSettingDTO(true, true);
//        UserDTO user = UserDTO.builder().userSetting(setting).build();
//        Notification notification = new Notification();
//        Mockito.doNothing().when(executorService).execute(runnableCaptor.capture());
//        Mockito.when(pushNotificationService.supports(setting)).thenReturn(true);
//        Mockito.when(smtpEmailService.supports(setting)).thenReturn(true);
//
//        notificationServiceCompound.sendNotification(user, notification);
//
//        Mockito.verify(executorService, Mockito.times(2)).execute(Mockito.any());
//        for (Runnable task : runnableCaptor.getAllValues()) {
//            task.run();
//        }
//        Mockito.verify(pushNotificationService).sendNotification(user, notification);
//        Mockito.verify(smtpEmailService).sendNotification(user, notification);
//    }
//
//}