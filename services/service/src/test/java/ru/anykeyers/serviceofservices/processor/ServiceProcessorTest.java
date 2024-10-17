//package ru.anykeyers.serviceofservices.processor;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import ru.anykeyers.serviceofservices.domain.service.ServiceUpdateRequest;
//import ru.anykeyers.serviceofservices.repository.ServiceRepository;
//import ru.anykeyers.serviceofservices.domain.service.ServiceEntity;
//import ru.anykeyers.serviceofservices.domain.service.ServiceCreateRequest;
//import ru.anykeyers.serviceofservices.domain.exception.ServiceNotFoundException;
//
//import java.util.Optional;
//
///**
// * Тесты для {@link ServiceProcessor}
// */
//@ExtendWith(MockitoExtension.class)
//class ServiceProcessorTest {
//
//    @Mock
//    private ServiceRepository serviceRepository;
//
//    @InjectMocks
//    private ServiceProcessorImpl serviceProcessor;
//
//    @Captor
//    private ArgumentCaptor<ServiceEntity> captor;
//
//    /**
//     * Тест сохранения услуги
//     */
//    @Test
//    void saveService() {
//        ServiceCreateRequest request = new ServiceCreateRequest(100L, "test-service", 3600, 250);
//        serviceProcessor.saveService(request);
//        Mockito.verify(serviceRepository).save(captor.capture());
//        ServiceEntity entity = captor.getValue();
//        Assertions.assertEquals(100L, entity.getCarWashId());
//        Assertions.assertEquals("test-service", entity.getName());
//        Assertions.assertEquals(3600, entity.getDuration());
//        Assertions.assertEquals(250, entity.getPrice());
//    }
//
//    /**
//     * Тест обновления несуществующей услуги
//     */
//    @Test
//    void updateNotFoundService() {
//        Mockito.when(serviceRepository.findById(1L)).thenReturn(Optional.empty());
//        ServiceNotFoundException exception = Assertions.assertThrows(
//                ServiceNotFoundException.class,
//                () -> serviceProcessor.updateService(new ServiceUpdateRequest(1L, null, 0, 0))
//        );
//        Assertions.assertEquals("Service not found with id: 1", exception.getReason());
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
//        Mockito.verify(serviceRepository, Mockito.never()).save(Mockito.any());
//    }
//
//    /**
//     * Тест обновления услуги
//     */
//    @Test
//    void updateService() {
//        ServiceEntity service = ServiceEntity.builder()
//                .id(1L)
//                .carWashId(1L)
//                .name("test-name")
//                .duration(100)
//                .price(200)
//                .build();
//        Mockito.when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
//        ServiceUpdateRequest request = new ServiceUpdateRequest(1L, "updated-name", 3600, 250);
//        serviceProcessor.updateService(request);
//        Mockito.verify(serviceRepository).save(captor.capture());
//        ServiceEntity entity = captor.getValue();
//        Assertions.assertEquals(1L, entity.getCarWashId());
//        Assertions.assertEquals("updated-name", entity.getName());
//        Assertions.assertEquals(3600, entity.getDuration());
//        Assertions.assertEquals(250, entity.getPrice());
//    }
//
//    /**
//     * Тест удаления услуги
//     */
//    @Test
//    void deleteService() {
//        serviceProcessor.deleteService(1L);
//        Mockito.verify(serviceRepository).deleteById(1L);
//    }
//
//}