package ru.anykeyers.configurationservice.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.domain.configuration.employee.EmployeeDTO;
import ru.anykeyers.commonsapi.remote.RemoteUserService;
import ru.anykeyers.configurationservice.domain.Employee;

import java.util.List;

/**
 * Маппер {@link Employee}
 */
@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    private final RemoteUserService remoteUserService;

    private final ConfigurationMapper configurationMapper;

    public List<EmployeeDTO> toDTO(List<Employee> employees) {
        return employees.stream().map(this::toDTO).toList();
    }

    public EmployeeDTO toDTO(Employee employee) {
        return new EmployeeDTO(
                remoteUserService.getUser(employee.getUserId()),
                employee.getStatus(),
                configurationMapper.toDTO(employee.getConfiguration())
        );
    }
}
