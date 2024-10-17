//package ru.anykeyers.configurationservice.service;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.anykeyers.configurationservice.domain.Employee;
//import ru.anykeyers.configurationservice.domain.Configuration;
//import ru.anykeyers.configurationservice.domain.exception.ConfigurationNotFoundException;
//import ru.anykeyers.configurationservice.repository.ConfigurationRepository;
//import ru.anykeyers.configurationservice.repository.EmployeeRepository;
//import ru.anykeyers.configurationservice.service.impl.EmployeeServiceImpl;
//
//import java.util.Optional;
//
///**
// * Тесты для {@link EmployeeService}
// */
//@ExtendWith(MockitoExtension.class)
//class EmployeeServiceTest {
//
//    @Mock
//    private EventService eventService;
//
//    @Mock
//    private EmployeeRepository employeeRepository;
//
//    @Mock
//    private ConfigurationRepository configurationRepository;
//
//    @InjectMocks
//    private EmployeeServiceImpl employeeService;
//
//    @Captor
//    private ArgumentCaptor<Employee> employeeCaptor;
//
//    /**
//     * Тест добавления работника автомойке
//     */
//    @Test
//    void addCarWashEmployee() {
//        Configuration existsConfiguration = Configuration.builder()
//                .id(1L)
//                .username("test-user")
//                .name("Best carwash")
//                .description("My car wash is really best")
//                .phoneNumber("+799999999")
//                .address("Lvov")
//                .openTime("08:00")
//                .closeTime("23:00")
//                .managementProcessOrders(false)
//                .build();
//
//        employeeService.addCarWashEmployee(existsConfiguration, 2L);
//
//        Mockito.verify(employeeRepository, Mockito.times(1)).save(employeeCaptor.capture());
//        Employee savedEmployee = employeeCaptor.getValue();
//        Assertions.assertEquals(savedEmployee.getUserId(), 2L);
//        Assertions.assertEquals(savedEmployee.getConfiguration().getId(), 1L);
//        Mockito.verify(eventService, Mockito.times(1)).sendEmployeeApplyEvent(Mockito.any());
//    }
//
//    /**
//     * Тест удаления работинка с несуществующей автомойки
//     */
//    @Test
//    void deleteEmployeeFromNotExistsConfiguration() {
//        Mockito.when(configurationRepository.findById(1L)).thenReturn(Optional.empty());
//        ConfigurationNotFoundException exception = Assertions.assertThrows(
//                ConfigurationNotFoundException.class, () -> employeeService.deleteEmployee(1L, 2L)
//        );
//        Assertions.assertEquals("Configuration with id 1 not found", exception.getMessage());
//        Mockito.verify(configurationRepository, Mockito.never()).save(Mockito.any());
//    }
//
//    /**
//     * Тест удаления работника с автомойки
//     */
//    @Test
//    void deleteEmployee() {
//        Configuration configuration = Configuration.builder().id(1L).build();
//        Mockito.when(configurationRepository.findById(1L)).thenReturn(Optional.of(configuration));
//
//        employeeService.deleteEmployee(1L, 2L);
//
//        Mockito.verify(employeeRepository).deleteByConfigurationAndUserId(configuration, 2L);
//    }
//
//}