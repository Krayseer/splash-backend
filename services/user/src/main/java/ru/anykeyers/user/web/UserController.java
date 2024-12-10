package ru.anykeyers.user.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.user.service.UserService;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.utils.JwtUtils;

import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.USER_URL)
@Tag(name = "Обработка пользователей")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Загрузить список всех пользователей")
    @GetMapping("/list")
    public Set<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Загрузить данные о текущем авторизованном пользователе")
    @GetMapping
    public User getUser(@AuthenticationPrincipal Jwt jwt) {
        return JwtUtils.extractUser(jwt);
    }

    @Operation(summary = "Загрузить данные о пользователе")
    @GetMapping("/{username}")
    public User getUser(
            @Parameter(description = "Имя пользователя") @PathVariable String username
    ) {
        return userService.getUser(username);
    }

    @Operation(summary = "Загрузить данные о пользователе по идентификатору")
    @GetMapping("/by-id/{id}")
    public User getUser(
            @Parameter(description = "Идентификатор пользователя") @PathVariable UUID id
    ) {
        return userService.getUser(id);
    }

    @Operation(summary = "Загрузить данные о нескольких пользователях")
    @GetMapping("/by-ids/{ids}")
    public Set<User> getUsers(
            @Parameter(description = "Идентификаторы пользователей") @PathVariable Set<UUID> ids
    ) {
        return userService.getUsers(ids);
    }

    @Operation(summary = "Создать пользователя")
    @PostMapping
    public void createUser(@RequestBody User user) {
        userService.addUser(user);
    }

    @Operation(summary = "Обновить пользователя")
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @Parameter(description = "Идентификатор пользователя") @PathVariable UUID userId
    ) {
        userService.deleteUser(userId);
    }

}
