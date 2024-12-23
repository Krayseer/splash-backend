package ru.anykeyers.commonsapi.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.domain.user.User;

import java.util.List;
import java.util.UUID;

/**
 * Удаленный сервис конфигурации автомоек
 */
@FeignClient(
        name = "configuration-service",
        path="/api/car-wash"
)
public interface RemoteConfigurationService {

    @GetMapping("/configuration/{id}")
    ConfigurationDTO getConfiguration(@PathVariable Long id);

    @GetMapping("/configuration/user/{userId}")
    ConfigurationDTO getUserConfiguration(@PathVariable UUID userId);

    @GetMapping("/employee/{id}")
    List<User> getEmployees(@PathVariable Long id);

}
