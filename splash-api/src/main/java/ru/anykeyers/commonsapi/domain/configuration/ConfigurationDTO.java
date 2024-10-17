package ru.anykeyers.commonsapi.domain.configuration;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.anykeyers.commonsapi.domain.Address;
import ru.anykeyers.commonsapi.validation.OnCreate;
import ru.anykeyers.commonsapi.validation.OnUpdate;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationDTO {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Long id;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private UUID userId;

    private OrganizationInfo organizationInfo;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Address address;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private List<Long> serviceIds;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private List<BoxDTO> boxes;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private List<String> photoUrls;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private List<MultipartFile> photos;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Instant openTime;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Instant closeTime;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private OrderProcessMode orderProcessMode;

}