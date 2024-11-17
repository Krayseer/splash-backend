package ru.anykeyers.configurationservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.anykeyers.commonsapi.domain.Address;
import ru.anykeyers.commonsapi.domain.configuration.OrderProcessMode;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationUpdateRequest {

    private String organizationInfo;

    private String openTime;

    private String closeTime;

    private OrderProcessMode orderProcessMode;

    private List<MultipartFile> photos;

    private Address address;

    private MultipartFile video;

}
