package ru.anykeyers.configurationservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.MessageQueue;
import ru.anykeyers.commonsapi.domain.configuration.employee.EmployeeDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.domain.Employee;
import ru.anykeyers.configurationservice.repository.EmployeeRepository;
import ru.anykeyers.configurationservice.service.ConfigurationService;
import ru.anykeyers.configurationservice.service.EmployeeService;
import ru.anykeyers.configurationservice.web.mapper.EmployeeMapper;

import java.util.List;
import java.util.UUID;

/**
 * Реализация сервиса обработки работников
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ConfigurationService configurationService;

    private final EmployeeMapper employeeMapper;

    @Override
    public List<Employee> getCarWashEmployees(Long carWashId) {
        Configuration configuration = configurationService.getConfiguration(carWashId);
        return employeeRepository.findByConfiguration(configuration);
    }

    @Override
    public List<Employee> getCarWashEmployees(User user) {
        Configuration configuration = configurationService.getConfiguration(user);
        return getCarWashEmployees(configuration.getId());
    }

    @Override
    public void addCarWashEmployee(Configuration configuration, UUID userId) {
        Employee employee = Employee.builder().userId(userId).configuration(configuration).build();
        employeeRepository.save(employee);
        EmployeeDTO employeeDTO = employeeMapper.toDTO(employee);
        kafkaTemplate.send(MessageQueue.EMPLOYEE_INVITATION_APPLY, employeeDTO);
        log.info("Add employee to car wash: {}", employee);
    }

    @Override
    public void deleteEmployee(Long carWashId, UUID userId) {
        Configuration configuration = configurationService.getConfiguration(carWashId);
        employeeRepository.deleteByConfigurationAndUserId(configuration, userId);
    }

}
