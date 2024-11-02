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
import ru.anykeyers.configurationservice.repository.EmployeeRepository;
import ru.anykeyers.configurationservice.service.ConfigurationService;
import ru.anykeyers.configurationservice.service.EmployeeService;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private final ConfigurationService configurationService;

    @Override
    public Set<User> getCarWashEmployees(Long carWashId) {
        Configuration configuration = configurationService.getConfiguration(carWashId);
        Set<UUID> employees = employeeRepository.findByConfiguration(configuration).stream()
                .map(Employee::getUserId)
                .collect(Collectors.toSet());
        return employees.isEmpty() ? Collections.EMPTY_SET : remoteUserService.getUsers(employees);
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
    public void deleteEmployee(Long carWashId, UUID userId) {
        Configuration configuration = configurationService.getConfiguration(carWashId);
        employeeRepository.deleteByConfigurationAndUserId(configuration, userId);
    }

}
