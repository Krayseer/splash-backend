package ru.anykeyers.configurationservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.task.Task;
import ru.anykeyers.commonsapi.task.TaskService;
import ru.anykeyers.commonsapi.utils.JwtUtils;
import ru.anykeyers.configurationservice.web.ControllerName;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.TASK_CONTROLLER)
@Tag(name = "Обработка фоновых задач")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Получить список фоновых задач пользователя")
    @GetMapping("/list")
    public List<Task> getAllTasks(@AuthenticationPrincipal Jwt jwt) {
        User user = JwtUtils.extractUser(jwt);
        return taskService.getTasks(user.getId());
    }

}
