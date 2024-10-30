package ru.anykeyers.commonsapi.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationInfoDTO;
import ru.anykeyers.commonsapi.domain.user.User;

import java.util.List;

/**
 * Удаленный сервис конфигурации автомоек
 */
@FeignClient(
        name = "configuration-service",
        path="/api/car-wash"
)
public interface RemoteConfigurationService {

    @GetMapping("/configuration/{id}")
    ConfigurationInfoDTO getConfiguration(@PathVariable Long id);

    @GetMapping("/employee/{id}")
    List<User> getEmployees(@PathVariable Long id);

}
