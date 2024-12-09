package ru.anykeyers.configurationservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.configurationservice.domain.invitation.InvitationState;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.domain.invitation.Invitation;
import ru.anykeyers.configurationservice.exception.ConfigurationNotFoundException;
import ru.anykeyers.configurationservice.exception.InvitationNotFoundException;
import ru.anykeyers.configurationservice.repository.InvitationRepository;
import ru.anykeyers.configurationservice.service.impl.InvitationServiceImpl;
import ru.anykeyers.configurationservice.web.dto.InvitationDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Тесты для {@link InvitationService}
 */
@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private ConfigurationService configurationService;

    @InjectMocks
    private InvitationServiceImpl invitationService;

    @Captor
    private ArgumentCaptor<Invitation> invitationCaptor;

    private final User user = User.builder().id(UUID.randomUUID()).build();

    /**
     * Тест добавления приглашения пользователю
     */
    @Test
    void addInvitation() {
        // Подготовка
        Long carWashId = 2L;
        InvitationDTO request = new InvitationDTO(user, carWashId, List.of("ROLE_MANAGER"));
        Configuration configuration = Configuration.builder().id(carWashId).build();
        Mockito.when(configurationService.getConfiguration(carWashId)).thenReturn(configuration);

        // Действие
        invitationService.addInvitation(request);

        // Проверка
        Mockito.verify(invitationRepository, Mockito.times(1)).save(invitationCaptor.capture());
        Invitation invitation = invitationCaptor.getValue();
        Assertions.assertEquals(user.getId(), invitation.getUserId());
        Assertions.assertEquals(carWashId, invitation.getConfiguration().getId());
        Assertions.assertEquals(InvitationState.SENT, invitation.getInvitationState());
        Assertions.assertEquals(invitation.getRoles(), List.of("ROLE_MANAGER"));
    }

    /**
     * Тест добавления приглашения от несуществующей автомойки
     */
    @Test
    void addInvitation_notExistsConfiguration() {
        // Подготовка
        Long carWashId = 2L;
        InvitationDTO request = new InvitationDTO(user, carWashId, Collections.emptyList());
        Mockito.when(configurationService.getConfiguration(carWashId)).thenThrow(ConfigurationNotFoundException.class);

        // Действие
        Assertions.assertThrows(
                ConfigurationNotFoundException.class, () -> invitationService.addInvitation(request)
        );

        // Проверка
        Mockito.verify(invitationRepository, Mockito.never()).save(Mockito.any());
    }

    /**
     * Тест одобрения приглашения
     */
    @Test
    void applyInvitation() {
        // Подготовка
        Long invitationId = 1L;
        Configuration configuration = Configuration.builder().id(1L).build();
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .userId(user.getId())
                .configuration(configuration)
                .roles(List.of("ROLE_WASHER"))
                .build();
        Mockito.when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        // Действие
        invitationService.applyInvitation(invitationId);

        // Проверка
        Mockito.verify(invitationRepository).save(invitationCaptor.capture());
        Assertions.assertEquals(InvitationState.ACCEPTED, invitationCaptor.getValue().getInvitationState());
        Mockito.verify(employeeService, Mockito.times(1)).addCarWashEmployee(configuration, user.getId());
    }

    /**
     * Тест одобрения несуществующего приглашения
     */
    @Test
    void applyInvitation_notExistsInvitation() {
        // Подготовка
        Long invitationId = 1L;
        Mockito.when(invitationRepository.findById(invitationId)).thenReturn(Optional.empty());

        // Действие
        InvitationNotFoundException exception = Assertions.assertThrows(
                InvitationNotFoundException.class, () -> invitationService.applyInvitation(invitationId)
        );

        // Проверка
        Assertions.assertEquals("Invitation with id 1 not found", exception.getMessage());
        Mockito.verify(employeeService, Mockito.never()).addCarWashEmployee(Mockito.any(), Mockito.any());
        Mockito.verify(invitationRepository, Mockito.never()).save(Mockito.any());
    }

    /**
     * Тест отклонения приглашения
     */
    @Test
    void declineInvitation() {
        // Подготовка
        Long invitationId = 1L;
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .userId(user.getId())
                .roles(List.of("ROLE_WASHER"))
                .build();
        Mockito.when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        // Действие
        invitationService.declineInvitation(invitationId);

        // Проверка
        Mockito.verify(invitationRepository).save(invitationCaptor.capture());
        Assertions.assertEquals(InvitationState.REJECTED, invitationCaptor.getValue().getInvitationState());
    }

    /**
     * Тест отклонения несуществующего приглашения
     */
    @Test
    void declineInvitation_notExistsInvitation() {
        // Подготовка
        Long invitationId = 1L;
        Mockito.when(invitationRepository.findById(invitationId)).thenReturn(Optional.empty());

        // Действие
        InvitationNotFoundException exception = Assertions.assertThrows(
                InvitationNotFoundException.class, () -> invitationService.declineInvitation(invitationId)
        );

        // Проверка
        Assertions.assertEquals("Invitation with id 1 not found", exception.getMessage());
        Mockito.verify(employeeService, Mockito.never()).addCarWashEmployee(Mockito.any(), Mockito.any());
        Mockito.verify(invitationRepository, Mockito.never()).save(Mockito.any());
    }

    /**
     * Тест удаления приглашения
     */
    @Test
    void deleteInvitation() {
        // Действие
        invitationService.deleteInvitation(1L);

        // Проверка
        Mockito.verify(invitationRepository).deleteById(1L);
    }

}