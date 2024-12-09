package ru.anykeyers.configurationservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.anykeyers.configurationservice.domain.Employee;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.exception.ConfigurationNotFoundException;
import ru.anykeyers.configurationservice.repository.EmployeeRepository;
import ru.anykeyers.configurationservice.service.impl.EmployeeServiceImpl;
import ru.anykeyers.configurationservice.web.mapper.EmployeeMapper;

import java.util.UUID;

/**
 * Тесты для {@link EmployeeService}
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ConfigurationService configurationService;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Captor
    private ArgumentCaptor<Employee> employeeCaptor;

    /**
     * Тест добавления работника автомойке
     */
    @Test
    void addCarWashEmployee() {
        // Подготовка
        UUID userId = UUID.randomUUID();
        Configuration configuration = Configuration.builder().id(1L).build();

        // Действие
        employeeService.addCarWashEmployee(configuration, userId);

        // Проверка
        Mockito.verify(kafkaTemplate).send(Mockito.eq("invitation-apply"), Mockito.any());
        Mockito.verify(employeeRepository, Mockito.times(1)).save(employeeCaptor.capture());
        Employee savedEmployee = employeeCaptor.getValue();
        Assertions.assertEquals(savedEmployee.getUserId(), userId);
        Assertions.assertEquals(savedEmployee.getConfiguration().getId(), 1L);
    }

    /**
     * Тест удаления работника с автомойки
     */
    @Test
    void deleteEmployee() {
        // Подготовка
        UUID userId = UUID.randomUUID();
        Configuration configuration = Configuration.builder().id(1L).build();
        Mockito.when(configurationService.getConfiguration(1L)).thenReturn(configuration);

        // Действие
        employeeService.deleteEmployee(1L, userId);

        // Проверка
        Mockito.verify(employeeRepository).deleteByConfigurationAndUserId(configuration, userId);
    }

    /**
     * Тест удаления работинка с несуществующей автомойки
     */
    @Test
    void deleteEmployee_notExistsConfiguration() {
        // Подготовка
        UUID userId = UUID.randomUUID();
        Mockito.when(configurationService.getConfiguration(1L)).thenThrow(ConfigurationNotFoundException.class);

        // Действие
        Assertions.assertThrows(
                ConfigurationNotFoundException.class, () -> employeeService.deleteEmployee(1L, userId)
        );

        // Проверка
        Mockito.verify(employeeRepository, Mockito.never()).deleteByConfigurationAndUserId(Mockito.any(), Mockito.any());
    }

}