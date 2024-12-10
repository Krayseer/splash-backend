package ru.anykeyers.notificationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.commonsapi.utils.JwtUtils;
import ru.anykeyers.notificationservice.domain.PushNotificationDTO;
import ru.anykeyers.notificationservice.service.impl.PushNotificationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.PUSH_CONTROLLER)
@Tag(name = "Обработка уведомлений")
public class PushNotificationController {

    private final PushNotificationService pushNotificationService;

    @Operation(summary = "Получить все уведомления авторизованного пользователя")
    @GetMapping
    public List<PushNotificationDTO> getNotifications(@AuthenticationPrincipal Jwt jwt) {
        return pushNotificationService.getNotifications(JwtUtils.extractUser(jwt));
    }

    @Operation(summary = "Удалить уведомление")
    @DeleteMapping("/{pushId}")
    public void deleteNotification(
            @Parameter(description = "Идентификатор уведомления") @PathVariable long pushId
    ) {
        pushNotificationService.deleteNotification(pushId);
    }

}
