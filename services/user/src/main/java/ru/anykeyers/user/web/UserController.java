package ru.anykeyers.user.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.user.service.UserService;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.utils.JwtUtils;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.USER_URL)
public class UserController {

    private final UserService userService;

    @GetMapping
    public User getUser(@AuthenticationPrincipal Jwt jwt) {
        return JwtUtils.extractUser(jwt);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @GetMapping("/by-id/{id}")
    public User getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        userService.addUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(UUID id) {
        userService.deleteUser(id);
    }

}
