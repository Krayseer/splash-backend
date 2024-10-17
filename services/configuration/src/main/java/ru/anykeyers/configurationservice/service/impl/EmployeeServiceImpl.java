package ru.anykeyers.configurationservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.MessageQueue;
import ru.anykeyers.commonsapi.domain.user.EmployeeDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.remote.RemoteUserService;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.domain.Employee;
import ru.anykeyers.configurationservice.exception.ConfigurationNotFoundException;
import ru.anykeyers.configurationservice.repository.ConfigurationRepository;
import ru.anykeyers.configurationservice.repository.EmployeeRepository;
import ru.anykeyers.configurationservice.service.EmployeeService;

import java.util.List;
import java.util.UUID;

/**
 * Реализация сервиса обработки работников
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final ModelMapper modelMapper;

    private final RemoteUserService remoteUserService;

    private final EmployeeRepository employeeRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ConfigurationRepository configurationRepository;

    @Override
    public List<User> getCarWashEmployees(Long carWashId) {
        Configuration configuration = configurationRepository.findById(carWashId).orElseThrow(
                () -> new ConfigurationNotFoundException(carWashId)
        );
        List<UUID> employees = employeeRepository.findByConfiguration(configuration).stream()
                .map(Employee::getUserId)
                .toList();
        return remoteUserService.getUsers(employees);
    }

    @Override
    public void addCarWashEmployee(Configuration configuration, UUID userId) {
        Employee employee = Employee.builder().userId(userId).configuration(configuration).build();
        employeeRepository.save(employee);
        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);
        kafkaTemplate.send(MessageQueue.EMPLOYEE_INVITATION_APPLY, employeeDTO);
        log.info("Add employee to car wash: {}", employee);
    }

    @Override
    public void deleteEmployee(Long carWashId, Long userId) {
        Configuration configuration = configurationRepository.findById(carWashId).orElseThrow(
                () -> new ConfigurationNotFoundException(carWashId)
        );
        employeeRepository.deleteByConfigurationAndUserId(configuration, userId);
    }

}
