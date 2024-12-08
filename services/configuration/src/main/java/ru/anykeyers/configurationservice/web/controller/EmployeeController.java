package ru.anykeyers.configurationservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.commonsapi.domain.configuration.employee.EmployeeDTO;
import ru.anykeyers.commonsapi.utils.JwtUtils;
import ru.anykeyers.configurationservice.service.EmployeeService;
import ru.anykeyers.configurationservice.web.ControllerName;
import ru.anykeyers.configurationservice.web.mapper.EmployeeMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.EMPLOYEE_CONTROLLER)
@Tag(name = "Обработка работников автомойки")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final EmployeeMapper employeeMapper;

    @Operation(summary = "Получить список работников автомойки авторизованного пользователя")
    @GetMapping
    public List<EmployeeDTO> getUserCarWashEmployees(@AuthenticationPrincipal Jwt jwt) {
        return employeeMapper.toDTO(employeeService.getCarWashEmployees(JwtUtils.extractUser(jwt)));
    }

    @Operation(summary = "Получить список работников автомойки")
    @GetMapping("/{carWashId}")
    public List<EmployeeDTO> getCarWashEmployees(@PathVariable Long carWashId) {
        return employeeMapper.toDTO(employeeService.getCarWashEmployees(carWashId));
    }

    @Operation(summary = "Уволить работника с автомойки")
    @DeleteMapping("/{carWashId}")
    public void deleteEmployee(@PathVariable("carWashId") Long carWashId,
                               @RequestParam("userId") UUID userId) {
        employeeService.deleteEmployee(carWashId, userId);
    }

}
