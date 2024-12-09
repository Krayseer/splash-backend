package ru.anykeyers.configurationservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anykeyers.commonsapi.domain.configuration.BoxDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.configurationservice.domain.Box;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.exception.BoxNotFoundException;
import ru.anykeyers.configurationservice.exception.ConfigurationNotFoundException;
import ru.anykeyers.configurationservice.repository.BoxRepository;
import ru.anykeyers.configurationservice.service.impl.BoxServiceImpl;

import java.util.Optional;
import java.util.UUID;

/**
 * Тесты для {@link BoxService}
 */
@ExtendWith(MockitoExtension.class)
class BoxServiceTest {

    @Mock
    private ConfigurationService configurationService;

    @Mock
    private BoxRepository boxRepository;

    @InjectMocks
    private BoxServiceImpl boxService;

    @Captor
    private ArgumentCaptor<Box> captor;

    private final User user = User.builder().id(UUID.randomUUID()).build();

    /**
     * Тест добавления бокса
     */
    @Test
    void addBox() {
        // Подготовка
        Long carWashId = 1L;
        BoxDTO request = new BoxDTO(1L, "First box super");
        Configuration configuration = Configuration.builder().id(carWashId).build();
        Mockito.when(configurationService.getConfiguration(user)).thenReturn(configuration);

        // Действие
        boxService.addBox(user, request);

        // Проверка
        Mockito.verify(boxRepository, Mockito.times(1)).save(Mockito.any());
    }

    /**
     * Тест добавления бокса несуществующей автомойке
     */
    @Test
    void addBox_notExistsCarWash() {
        // Подготовка
        BoxDTO request = new BoxDTO(1L, "First box super");
        Mockito.when(configurationService.getConfiguration(user)).thenThrow(ConfigurationNotFoundException.class);

        // Действие
        Assertions.assertThrows(
                ConfigurationNotFoundException.class, () -> boxService.addBox(user, request)
        );

        // Проверка
        Mockito.verify(boxRepository, Mockito.times(0)).save(Mockito.any());
    }

    /**
     * Тест обновления бокса
     */
    @Test
    void updateBox() {
        BoxDTO request = new BoxDTO(1L, "Updated name");
        Box box = Box.builder().id(1L).name("Name").build();
        Mockito.when(boxRepository.findById(1L)).thenReturn(Optional.of(box));

        boxService.updateBox(request);

        Mockito.verify(boxRepository, Mockito.times(1)).save(captor.capture());
        Box capturedBox = captor.getValue();
        Assertions.assertEquals(1L, capturedBox.getId());
        Assertions.assertEquals("Updated name", capturedBox.getName());
    }

    /**
     * Тест обновления несуществующего бокса
     */
    @Test
    void updateBox_notExistsBox() {
        BoxDTO request = new BoxDTO(1L, "Updated name");
        Mockito.when(boxRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(
                BoxNotFoundException.class, () -> boxService.updateBox(request)
        );
    }

    /**
     * Тест удаления бокса
     */
    @Test
    void deleteBox() {
        boxService.deleteBox(1L);
        Mockito.verify(boxRepository, Mockito.times(1)).deleteById(1L);
    }

}