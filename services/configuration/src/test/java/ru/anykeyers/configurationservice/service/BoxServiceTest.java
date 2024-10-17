//package ru.anykeyers.configurationservice.service;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import ru.anykeyers.commonsapi.domain.Address;
//import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
//import ru.anykeyers.commonsapi.domain.configuration.OrderProcessMode;
//import ru.anykeyers.commonsapi.domain.configuration.OrganizationInfo;
//import ru.anykeyers.commonsapi.domain.configuration.TypeOrganization;
//import ru.anykeyers.configurationservice.domain.Box;
//import ru.anykeyers.configurationservice.domain.Configuration;
//import ru.anykeyers.configurationservice.repository.BoxRepository;
//import ru.anykeyers.configurationservice.repository.ConfigurationRepository;
//import ru.anykeyers.configurationservice.service.impl.BoxServiceImpl;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.UUID;
//
///**
// * Тесты для {@link BoxService}
// */
//@ExtendWith(MockitoExtension.class)
//class BoxServiceTest {
//
//    @Mock
//    private BoxRepository boxRepository;
//
//    @Mock
//    private ConfigurationRepository configurationRepository;
//
//    @InjectMocks
//    private BoxServiceImpl boxService;
//
//    @Captor
//    private ArgumentCaptor<Box> captor;
//
//    @Test
//    void testMappingConfiguration() {
//        Configuration config = configuration();
//        ConfigurationDTO configurationDTO = configurationDTO();
//        Configuration configuration = new ModelMapper().map(configurationDTO, Configuration.class);
//        ConfigurationDTO configurationDTOFromConfiguration = new ModelMapper().map(config, ConfigurationDTO.class);
//    }
//
//    private Configuration configuration() {
//        return Configuration.builder()
//                .id(5L)
//                .userId(UUID.randomUUID())
//                .organizationInfo(
//                        OrganizationInfo.builder()
//                                .tin("tin123")
//                                .typeOrganization(TypeOrganization.IP)
//                                .name("Organization123")
//                                .description("Some desc")
//                                .phoneNumber("+799999999")
//                                .email("testEmail@gmail.com")
//                                .build()
//                )
//                .boxes(
//                        List.of(
//                                Box.builder()
//                                        .id(2L)
//                                        .name("Hello world")
//                                        .configuration(null)
//                                        .build()
//                        )
//                )
//                .address(
//                        new Address("123", "124", "125")
//                )
//                .photoUrls(List.of("photo1", "photo2"))
//                .orderProcessMode(OrderProcessMode.AUTO)
//                .build();
//    }
//
//    private ConfigurationDTO configurationDTO() {
//        return ConfigurationDTO.builder()
//                .id(5L)
//                .userId(UUID.randomUUID())
//                .organizationInfo(
//                        OrganizationInfo.builder()
//                                .tin("tin123")
//                                .typeOrganization(TypeOrganization.IP)
//                                .name("Organization123")
//                                .description("Some desc")
//                                .phoneNumber("+799999999")
//                                .email("testEmail@gmail.com")
//                                .build()
//                )
//                .address(
//                        new Address("123", "124", "125")
//                )
//                .serviceIds(List.of(1L, 2L, 3L))
//                .photoUrls(List.of("photo1", "photo2"))
//                .openTime(Instant.ofEpochMilli(100))
//                .closeTime(Instant.ofEpochMilli(200))
//                .orderProcessMode(OrderProcessMode.AUTO)
//                .build();
//    }
//
////    /**
////     * Тест добавления бокса несуществующей автомойке
////     */
////    @Test
////    void addBoxWithNotExistsCarWash() {
////        Long carWashId = 1L;
////        Mockito.when(configurationRepository.findById(carWashId)).thenReturn(Optional.empty());
////        ConfigurationNotFoundException exception = Assertions.assertThrows(
////                ConfigurationNotFoundException.class, () -> boxService.addBox(carWashId, null)
////        );
////        Assertions.assertEquals("Configuration with id 1 not found", exception.getMessage());
////    }
////
////    /**
////     * Тест добавления бокса
////     */
////    @Test
////    void addBox() {
////        BoxRequest request = new BoxRequest("First box super");
////        Long carWashId = 1L;
////        Configuration configuration = Configuration.builder().id(carWashId).build();
////        Mockito.when(configurationRepository.findById(carWashId)).thenReturn(Optional.of(configuration));
////        boxService.addBox(carWashId, request);
////        Mockito.verify(boxRepository, Mockito.times(1)).save(Mockito.any());
////    }
////
////    /**
////     * Тест обновления несуществующего бокса
////     */
////    @Test
////    void updateNotExistsBox() {
////        Mockito.when(boxRepository.findById(1L)).thenReturn(Optional.empty());
////        BoxNotFoundException exception = Assertions.assertThrows(
////                BoxNotFoundException.class, () -> boxService.updateBox(1L, new BoxRequest())
////        );
////        Assertions.assertEquals("Box not found: 1", exception.getMessage());
////    }
////
////    /**
////     * Тест обновления бокса
////     */
////    @Test
////    void updateBox() {
////        BoxRequest request = new BoxRequest("Updated name");
////        Box box = Box.builder().id(1L).name("Name").build();
////        Mockito.when(boxRepository.findById(1L)).thenReturn(Optional.of(box));
////
////        boxService.updateBox(1L, request);
////
////        Mockito.verify(boxRepository, Mockito.times(1)).save(captor.capture());
////        Box capturedBox = captor.getValue();
////        Assertions.assertEquals(1L, capturedBox.getId());
////        Assertions.assertEquals("Updated name", capturedBox.getName());
////    }
////
////    /**
////     * Тест удаления бокса
////     */
////    @Test
////    void deleteBox() {
////        boxService.deleteBox(1L);
////        Mockito.verify(boxRepository, Mockito.times(1)).deleteById(1L);
////    }
//
//}