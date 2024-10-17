package ru.anykeyers.commonsapi.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO{
    private UUID userId;
    private ConfigurationDTO configuration;
}