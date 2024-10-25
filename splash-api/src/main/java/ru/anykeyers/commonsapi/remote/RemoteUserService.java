package ru.anykeyers.commonsapi.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.anykeyers.commonsapi.domain.user.User;

import java.util.Set;
import java.util.UUID;

/**
 * Удаленный сервис обработки пользователей
 */
@FeignClient(
        name = "user-service",
        path = "/api/user"
)
public interface RemoteUserService {

    @GetMapping("/{username}")
    User getUser(@PathVariable("username") String username);

    @GetMapping("/by-id/{id}")
    User getUser(@PathVariable("id") UUID id);

    //TODO: реализовать в том числе в контроллере
    @GetMapping
    Set<User> getUsers(Set<UUID> employees);
}
