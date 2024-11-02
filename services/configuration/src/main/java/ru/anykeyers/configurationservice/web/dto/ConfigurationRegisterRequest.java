package ru.anykeyers.configurationservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.anykeyers.commonsapi.domain.configuration.OrganizationInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationRegisterRequest {

    private String tin;

    private String email;

    private OrganizationInfo.Type type;

}
