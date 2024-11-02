package ru.anykeyers.configurationservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.configurationservice.service.EmployeeService;
import ru.anykeyers.configurationservice.web.ControllerName;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.EMPLOYEE_CONTROLLER)
@Tag(name = "Обработка работников автомойки")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Получить список работников автомойки")
    @GetMapping("/{carWashId}")
    public Set<User> getEmployees(@PathVariable Long carWashId) {
        return employeeService.getCarWashEmployees(carWashId);
    }

    @Operation(summary = "Уволить работника с автомойки")
    @DeleteMapping("/{carWashId}")
    public void deleteEmployee(@PathVariable("carWashId") Long carWashId,
                               @RequestParam("userId") UUID userId) {
        employeeService.deleteEmployee(carWashId, userId);
    }

}
