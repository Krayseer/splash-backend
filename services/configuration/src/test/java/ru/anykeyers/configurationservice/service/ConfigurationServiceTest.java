//package ru.anykeyers.configurationservice.service;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//import ru.anykeyers.commonsapi.remote.RemoteStorageService;
//import ru.anykeyers.commonsapi.domain.configuration.TypeOrganization;
//import ru.anykeyers.configurationservice.domain.Configuration;
//import ru.anykeyers.configurationservice.web.dto.ConfigurationRegisterRequest;
//import ru.anykeyers.configurationservice.web.dto.ConfigurationUpdateRequest;
//import ru.anykeyers.configurationservice.domain.exception.UserNotFoundConfigurationException;
//import ru.anykeyers.configurationservice.repository.ConfigurationRepository;
//import ru.anykeyers.configurationservice.service.impl.ConfigurationServiceImpl;
//
//import java.util.List;
//import java.util.Optional;
//
///**
// * Тесты для {@link ConfigurationService}
// */
//@ExtendWith(MockitoExtension.class)
//class ConfigurationServiceTest {
//
//    @Mock
//    private TaskManager worker;
//
//    @Mock
//    private RemoteStorageService remoteStorageService;
//
//    @Mock
//    private ConfigurationRepository configurationRepository;
//
//    @InjectMocks
//    private ConfigurationServiceImpl configurationService;
//
//    @Captor
//    private ArgumentCaptor<Runnable> runnableCaptor;
//
//    @Captor
//    private ArgumentCaptor<Configuration> configurationCaptor;
//
//    /**
//     * Тест успешной регистрации автомойки
//     */
//    @Test
//    void registerConfiguration() {
//        String username = "test-user";
//        ConfigurationRegisterRequest request = new ConfigurationRegisterRequest(
//                "124751045", TypeOrganization.OOO, "123@email.com"
//        );
//        configurationService.registerConfiguration(username, request);
//        ArgumentCaptor<Configuration> captor = ArgumentCaptor.forClass(Configuration.class);
//        Mockito.verify(configurationRepository, Mockito.times(1)).save(captor.capture());
//        Configuration savedConfiguration = captor.getValue();
//
//        Assertions.assertEquals("test-user", savedConfiguration.getUsername());
//        Assertions.assertEquals("124751045", savedConfiguration.getTin());
//        Assertions.assertEquals(TypeOrganization.OOO, savedConfiguration.getTypeOrganization());
//        Assertions.assertEquals("123@email.com", savedConfiguration.getEmail());
//    }
//
//    /**
//     * Тест обновления несуществующей конфигурации
//     */
//    @Test
//    void updateNotExistsConfiguration() {
//        Mockito.when(configurationRepository.findByUsername("test-user")).thenReturn(Optional.empty());
//        UserNotFoundConfigurationException exception = Assertions.assertThrows(
//                UserNotFoundConfigurationException.class,
//                () -> configurationService.updateConfiguration("test-user", new ConfigurationUpdateRequest())
//        );
//        Assertions.assertEquals("Configuration for user test-user not found", exception.getMessage());
//        Mockito.verify(configurationRepository, Mockito.never()).save(Mockito.any());
//    }
//
//    /**
//     * Тест обновления существующей конфигурации
//     */
//    @Test
//    void updateConfiguration() {
//        Configuration existsConfiguration = Configuration.builder()
//                .username("test-user")
//                .name("Best carwash")
//                .description("My car wash is really best")
//                .phoneNumber("+799999999")
//                .address("Lvov")
//                .openTime("08:00")
//                .closeTime("23:00")
//                .managementProcessOrders(false)
//                .build();
//        ConfigurationUpdateRequest request = new ConfigurationUpdateRequest(
//                "BestIce",
//                "My car wash is really best",
//                "+795214545",
//                "Lvov",
//                "123",
//                "456",
//                "05:00",
//                "23:00",
//                null,
//                true,
//                true
//        );
//        Mockito.when(configurationRepository.findByUsername("test-user")).thenReturn(Optional.ofNullable(existsConfiguration));
//
//        configurationService.updateConfiguration("test-user", request);
//
//        Mockito.verify(configurationRepository).save(configurationCaptor.capture());
//        Configuration savedConfiguration = configurationCaptor.getValue();
//
//        Assertions.assertEquals("test-user", savedConfiguration.getUsername());
//        Assertions.assertEquals("BestIce", savedConfiguration.getName());
//        Assertions.assertEquals("My car wash is really best", savedConfiguration.getDescription());
//        Assertions.assertEquals("+795214545", savedConfiguration.getPhoneNumber());
//        Assertions.assertEquals("Lvov", savedConfiguration.getAddress());
//        Assertions.assertEquals("123", savedConfiguration.getLongitude());
//        Assertions.assertEquals("456", savedConfiguration.getLatitude());
//        Assertions.assertEquals("05:00", savedConfiguration.getOpenTime());
//        Assertions.assertEquals("23:00", savedConfiguration.getCloseTime());
//        Assertions.assertTrue(savedConfiguration.isManagementProcessOrders());
//    }
//
//    /**
//     * Тест обновления конфигурации автомойки с новыми фотографиями
//     */
//    @Test
//    void updateConfigurationWithNewPhotos() {
//        Configuration configuration = Configuration.builder().id(2L).username("test-user").build();
//        List<MultipartFile> photos = List.of(
//                new MockMultipartFile("1", "2", "3", new byte[] {1, 2, 3})
//        );
//        ConfigurationUpdateRequest request = new ConfigurationUpdateRequest(
//                "BestIce",
//                "My car wash is really best",
//                "+795214545",
//                "Lvov",
//                "123",
//                "456",
//                "05:00",
//                "23:00",
//                photos,
//                true,
//                true
//        );
//        List<String> photoUrls = List.of("photo-url");
//        Mockito.when(configurationRepository.findByUsername("test-user")).thenReturn(Optional.of(configuration));
//        Mockito.doNothing().when(worker).addTask(runnableCaptor.capture());
//        Mockito.when(remoteStorageService.uploadPhotos(photos)).thenReturn(ResponseEntity.of(Optional.of(photoUrls)));
//        Mockito.when(configurationRepository.findById(2L)).thenReturn(Optional.of(configuration));
//
//        configurationService.updateConfiguration("test-user", request);
//
//        Runnable task = runnableCaptor.getValue();
//        task.run();
//
//        Mockito.verify(configurationRepository, Mockito.times(2)).save(configurationCaptor.capture());
//        Configuration capturedConfiguration = configurationCaptor.getValue();
//        Assertions.assertEquals(photoUrls, capturedConfiguration.getPhotoUrls());
//    }
//
//    /**
//     * Тест удаления несуществующей конфигурации автомойки пользователя
//     */
//    @Test
//    void deleteNotExistsUserConfiguration() {
//        Mockito.when(configurationRepository.findByUsername("test-user")).thenReturn(Optional.empty());
//        UserNotFoundConfigurationException exception = Assertions.assertThrows(
//                UserNotFoundConfigurationException.class,
//                () -> configurationService.updateConfiguration("test-user", new ConfigurationUpdateRequest())
//        );
//        Assertions.assertEquals("Configuration for user test-user not found", exception.getMessage());
//        Mockito.verify(configurationRepository, Mockito.never()).save(Mockito.any());
//    }
//
//    /**
//     * Тест успешного удаления конфигурации автомойки пользователя
//     */
//    @Test
//    void deleteConfiguration() {
//        Configuration configuration = Configuration.builder().id(2L).username("test-user").build();
//        Mockito.when(configurationRepository.findByUsername("test-user")).thenReturn(Optional.of(configuration));
//
//        configurationService.deleteConfiguration("test-user");
//
//        Mockito.verify(configurationRepository).delete(configurationCaptor.capture());
//        Assertions.assertEquals(configuration, configurationCaptor.getValue());
//    }
//
//}