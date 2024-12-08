//package ru.anykeyers.notificationservice.processor;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.anykeyers.commonsapi.domain.configuration.employee.EmployeeDTO;
//import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
//import ru.anykeyers.commonsapi.domain.user.UserDTO;
//import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;
//import ru.anykeyers.commonsapi.remote.RemoteUserService;
//import ru.anykeyers.notificationservice.domain.Notification;
//import ru.anykeyers.notificationservice.service.NotificationServiceCompound;
//
///**
// * Тесты для {@link EmployeeProcessor}
// */
//@ExtendWith(MockitoExtension.class)
//class EmployeeProcessorTest {
//
//    @Mock
//    private RemoteUserService remoteUserService;
//
//    @Mock
//    private RemoteConfigurationService remoteConfigurationService;
//
//    @Mock
//    private NotificationServiceCompound notificationServiceCompound;
//
//    @InjectMocks
//    private EmployeeProcessor employeeProcessor;
//
//    /**
//     * Тест отправки уведомления о принятии работника на автомойку
//     */
//    @Test
//    void processEmployeeApply() {
//        Long carWashId = 2L;
//        ConfigurationDTO configuration = ConfigurationDTO.builder()
//                .id(carWashId)
//                .organizationInfo("car-wash")
//                .address("address")
//                .username("car-wash-holder")
//                .build();
//        UserDTO employee = UserDTO.builder()
//                .id(1L)
//                .build();
//        EmployeeDTO employeeDTO = new EmployeeDTO(1L, carWashId);
//        UserDTO carWashHolder = UserDTO.builder()
//                .id(3L)
//                .username("car-wash-holder")
//                .build();
//        Mockito.when(remoteConfigurationService.getConfiguration(carWashId)).thenReturn(configuration);
//        Mockito.when(remoteUserService.getUser(1L)).thenReturn(employee);
//        Mockito.when(remoteUserService.getUser("car-wash-holder")).thenReturn(carWashHolder);
//
//        employeeProcessor.processEmployeeApply(employeeDTO);
//
//        Notification expectedNotificationEmployee = new Notification(
//                "Вы приняты на работу", """
//                Поздравляем, вы стали работником автомойки!
//                Автомойка: car-wash
//                Адрес: address
//                """
//        );
//        Notification expectedNotificationCarWashHolder = new Notification(
//                "Новый работник", """
//                У вас новый работник!
//                Имя: null
//                Фамилия: null
//                Роли: null
//                """
//        );
//        Mockito.verify(notificationServiceCompound).sendNotification(employee, expectedNotificationEmployee);
//        Mockito.verify(notificationServiceCompound).sendNotification(carWashHolder, expectedNotificationCarWashHolder);
//    }
//
//}