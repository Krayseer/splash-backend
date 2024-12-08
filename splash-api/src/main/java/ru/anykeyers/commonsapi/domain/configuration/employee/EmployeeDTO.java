package ru.anykeyers.commonsapi.domain.configuration.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.domain.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private User user;

    private EmployeeStatus status;

    private ConfigurationDTO configuration;

}