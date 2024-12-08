package ru.anykeyers.serviceofservices.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.anykeyers.commonsapi.domain.service.ServiceDTO;
import ru.anykeyers.serviceofservices.repository.ServiceRepository;
import ru.anykeyers.serviceofservices.domain.ServiceEntity;
import ru.anykeyers.serviceofservices.domain.exception.ServiceNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Тесты для {@link ServiceProcessor}
 */
@ExtendWith(MockitoExtension.class)
class ServiceProcessorTest {

    @Mock
    private ServiceRepository serviceRepository;

    private ServiceProcessorImpl serviceProcessor;

    @Captor
    private ArgumentCaptor<ServiceEntity> captor;

    @BeforeEach
    void setUp() {
        serviceProcessor = new ServiceProcessorImpl(serviceRepository, new VersionGenerator());
    }

    /**
     * Тест сохранения услуги
     */
    @Test
    void saveService() {
        // Подготовка
        ServiceDTO request = new ServiceDTO(0L, "test-service", 3600, 250);

        // Действие
        serviceProcessor.saveService(100L, request);

        // Проверка
        Mockito.verify(serviceRepository).save(captor.capture());
        ServiceEntity entity = captor.getValue();
        Assertions.assertEquals(100L, entity.getCarWashId());
        Assertions.assertEquals("test-service", entity.getName());
        Assertions.assertEquals(3600, entity.getDuration());
        Assertions.assertEquals(250, entity.getPrice());
    }

    /**
     * Тест обновления несуществующей услуги
     */
    @Test
    void updateNotFoundService() {
        // Подготовка
        Mockito.when(serviceRepository.findById(1L)).thenReturn(Optional.empty());

        // Действие + проверка
        ServiceNotFoundException exception = Assertions.assertThrows(
                ServiceNotFoundException.class,
                () -> serviceProcessor.updateService(new ServiceDTO(1L, null, 0, 0))
        );
        Assertions.assertEquals("Service not found with id: 1", exception.getReason());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Mockito.verify(serviceRepository, Mockito.never()).save(Mockito.any());
    }

    /**
     * Тест обновления услуги
     */
    @Test
    void updateService() {
        // Подготовка
        ServiceEntity service = ServiceEntity.builder()
                .id(1L)
                .carWashId(1L)
                .name("test-name")
                .duration(100)
                .price(200)
                .version("1.0")
                .actual(true)
                .build();
        Mockito.when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        ServiceDTO request = new ServiceDTO(1L, "updated-name", 3600, 250);

        // Действие
        serviceProcessor.updateService(request);

        // Проверка
        Mockito.verify(serviceRepository, Mockito.times(2)).save(captor.capture());
        List<ServiceEntity> capturedEntities = captor.getAllValues();

        ServiceEntity originalService = capturedEntities.get(0);
        Assertions.assertFalse(originalService.isActual());

        ServiceEntity updatedService = capturedEntities.get(1);
        Assertions.assertEquals(1L, updatedService.getCarWashId());
        Assertions.assertEquals("updated-name", updatedService.getName());
        Assertions.assertEquals(3600, updatedService.getDuration());
        Assertions.assertEquals(250, updatedService.getPrice());
        Assertions.assertEquals("1.1", updatedService.getVersion());
        Assertions.assertTrue(updatedService.isActual());
    }

    /**
     * Тест удаления услуги
     */
    @Test
    void deleteService() {
        serviceProcessor.deleteService(1L);
        Mockito.verify(serviceRepository).deleteById(1L);
    }

}