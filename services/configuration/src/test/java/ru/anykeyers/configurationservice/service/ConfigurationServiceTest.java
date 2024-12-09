package ru.anykeyers.configurationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.anykeyers.commonsapi.domain.Address;
import ru.anykeyers.commonsapi.domain.configuration.OrderProcessMode;
import ru.anykeyers.commonsapi.domain.configuration.OrganizationInfo;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.exception.UserNotFoundConfigurationException;
import ru.anykeyers.configurationservice.web.dto.ConfigurationRegisterRequest;
import ru.anykeyers.configurationservice.web.dto.ConfigurationUpdateRequest;
import ru.anykeyers.configurationservice.repository.ConfigurationRepository;
import ru.anykeyers.configurationservice.service.impl.ConfigurationServiceImpl;
import ru.krayseer.storageclient.FileStorageClient;

import java.util.*;
import java.util.function.Consumer;

/**
 * Тесты для {@link ConfigurationService}
 */
@ExtendWith(MockitoExtension.class)
class ConfigurationServiceTest {

    @Mock
    private ConfigurationRepository configurationRepository;

    @Mock
    private FileStorageClient fileStorageClient;

    private final ModelMapper modelMapper = new ModelMapper();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ConfigurationServiceImpl configurationService;

    @Captor
    private ArgumentCaptor<Configuration> configurationCaptor;

    private final UUID userId = UUID.randomUUID();

    private final User user = User.builder().id(userId).build();

    @BeforeEach
    public void setUp() {
        configurationService = new ConfigurationServiceImpl(
                modelMapper, configurationRepository, fileStorageClient, objectMapper, null
        );
    }

    /**
     * Тест успешной регистрации автомойки
     */
    @Test
    void registerConfiguration() {
        // Подготовка
        ConfigurationRegisterRequest request = new ConfigurationRegisterRequest(
                "124751045", "123@email.com", OrganizationInfo.Type.OOO
        );

        // Действие
        configurationService.registerConfiguration(user, request);

        // Проверка
        Mockito.verify(configurationRepository).save(configurationCaptor.capture());
        Configuration savedConfiguration = configurationCaptor.getValue();
        OrganizationInfo info = savedConfiguration.getOrganizationInfo();

        Assertions.assertEquals(userId, savedConfiguration.getUserId());
        Assertions.assertEquals("124751045", info.getTin());
        Assertions.assertEquals(OrganizationInfo.Type.OOO, info.getType());
        Assertions.assertEquals("123@email.com", info.getEmail());
    }

    /**
     * Тест обновления существующей конфигурации
     */
    @Test
    @SneakyThrows
    void updateConfiguration() {
        // Подготовка
        Configuration existsConfiguration = Configuration.builder()
                .userId(userId)
                .organizationInfo(
                        OrganizationInfo.builder()
                                .name("Best carwash")
                                .description("My car wash is really best")
                                .phoneNumber("+799999999")
                                .build()
                )
                .address(new Address(124, 142, "Lvov"))
                .openTime("08:00")
                .closeTime("23:00")
                .orderProcessMode(OrderProcessMode.MANAGER_PROCESSING)
                .build();

        OrganizationInfo updatedOrganizationInfo = OrganizationInfo.builder()
                .name("BestIce")
                .description("My car wash is really best")
                .phoneNumber("+795214545")
                .build();
        Address updatedAddress = new Address(555, 666, "new Address");

        ConfigurationUpdateRequest request = new ConfigurationUpdateRequest(
                objectMapper.writeValueAsString(updatedOrganizationInfo),
                "05:00",
                "20:00",
                OrderProcessMode.SELF_SERVICE,
                Collections.emptyList(),
                objectMapper.writeValueAsString(updatedAddress),
                null
        );
        Mockito.when(configurationRepository.findByUserId(user.getId())).thenReturn(Optional.ofNullable(existsConfiguration));

        // Действие
        configurationService.updateConfiguration(user, request);

        // Проверка
        Mockito.verify(configurationRepository).save(configurationCaptor.capture());
        Configuration savedConfiguration = configurationCaptor.getValue();
        OrganizationInfo info = savedConfiguration.getOrganizationInfo();

        Assertions.assertEquals(userId, savedConfiguration.getUserId());
        Assertions.assertEquals("BestIce", info.getName());
        Assertions.assertEquals("My car wash is really best", info.getDescription());
        Assertions.assertEquals("+795214545", info.getPhoneNumber());
        Assertions.assertEquals("new Address", savedConfiguration.getAddress().getAddress());
        Assertions.assertEquals(555, savedConfiguration.getAddress().getLongitude());
        Assertions.assertEquals(666, savedConfiguration.getAddress().getLatitude());
        Assertions.assertEquals("05:00", savedConfiguration.getOpenTime());
        Assertions.assertEquals("20:00", savedConfiguration.getCloseTime());
        Assertions.assertEquals(OrderProcessMode.SELF_SERVICE, savedConfiguration.getOrderProcessMode());
    }

    /**
     * Тест обновления несуществующей конфигурации
     */
    @Test
    void updateConfiguration_notExistsConfiguration() {
        // Подготовка
        Mockito.when(configurationRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Действие
        UserNotFoundConfigurationException exception = Assertions.assertThrows(
                UserNotFoundConfigurationException.class,
                () -> configurationService.updateConfiguration(user, new ConfigurationUpdateRequest())
        );

        // Проверка
        Assertions.assertEquals(String.format("Configuration for user %s not found", userId), exception.getMessage());
        Mockito.verify(configurationRepository, Mockito.never()).save(Mockito.any());
    }

    /**
     * Тест обновления конфигурации автомойки с новыми фотографиями
     */
    @Test
    @SneakyThrows
    void updateConfiguration_withUploadNewPhotos() {
        // Подготовка
        Configuration configuration = Configuration.builder().id(2L).userId(userId).build();
        List<MultipartFile> photos = List.of(
                new MockMultipartFile("1", "2", "3", new byte[] {1, 2, 3})
        );
        OrganizationInfo updatedOrganizationInfo = OrganizationInfo.builder()
                .name("BestIce")
                .description("My car wash is really best")
                .phoneNumber("+795214545")
                .build();
        Address updatedAddress = new Address(555, 666, "new Address");
        ConfigurationUpdateRequest request = new ConfigurationUpdateRequest(
                objectMapper.writeValueAsString(updatedOrganizationInfo),
                StringUtils.EMPTY,
                StringUtils.EMPTY,
                OrderProcessMode.SELF_SERVICE,
                photos,
                objectMapper.writeValueAsString(updatedAddress),
                null
        );

        Mockito.doAnswer(invocation -> {
            Consumer<List<String>> callback = invocation.getArgument(1);
            callback.accept(Arrays.asList("url1", "url2"));
            return null;
        }).when(fileStorageClient).uploadPhotos(Mockito.anyList(), Mockito.any());
        Mockito.when(configurationRepository.findByUserId(userId)).thenReturn(Optional.of(configuration));

        // Действие
        configurationService.updateConfiguration(user, request);
        Thread.sleep(500);

        // Проверка
        Mockito.verify(configurationRepository, Mockito.times(2)).save(configurationCaptor.capture());
        Configuration capturedConfiguration = configurationCaptor.getValue();
        Assertions.assertEquals(List.of("url1", "url2"), capturedConfiguration.getPhotoUrls());
    }

    /**
     * Тест успешного удаления конфигурации автомойки пользователя
     */
    @Test
    void deleteConfiguration() {
        // Подготовка
        Configuration configuration = Configuration.builder().id(2L).build();
        Mockito.when(configurationRepository.findByUserId(userId)).thenReturn(Optional.of(configuration));

        // Действие
        configurationService.deleteConfiguration(user);

        // Проверка
        Mockito.verify(configurationRepository).delete(configurationCaptor.capture());
        Assertions.assertEquals(configuration, configurationCaptor.getValue());
    }

    /**
     * Тест удаления несуществующей конфигурации автомойки пользователя
     */
    @Test
    void deleteConfiguration_notExistsUserConfiguration() {
        // Подготовка
        Mockito.when(configurationRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Действие
        UserNotFoundConfigurationException exception = Assertions.assertThrows(
                UserNotFoundConfigurationException.class,
                () -> configurationService.updateConfiguration(user, new ConfigurationUpdateRequest())
        );

        // Проверка
        Assertions.assertEquals(String.format("Configuration for user %s not found", userId), exception.getMessage());
        Mockito.verify(configurationRepository, Mockito.never()).save(Mockito.any());
    }

}