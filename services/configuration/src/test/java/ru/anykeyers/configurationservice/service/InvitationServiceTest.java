//package ru.anykeyers.configurationservice.service;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.anykeyers.commonsapi.domain.user.UserDTO;
//import ru.anykeyers.configurationservice.domain.invitation.InvitationState;
//import ru.anykeyers.commonsapi.remote.RemoteUserService;
//import ru.anykeyers.configurationservice.domain.Configuration;
//import ru.anykeyers.configurationservice.domain.invitation.Invitation;
//import ru.anykeyers.configurationservice.web.dto.InvitationRequest;
//import ru.anykeyers.configurationservice.domain.exception.ConfigurationNotFoundException;
//import ru.anykeyers.configurationservice.domain.exception.InvitationNotFoundException;
//import ru.anykeyers.configurationservice.repository.ConfigurationRepository;
//import ru.anykeyers.configurationservice.repository.InvitationRepository;
//import ru.anykeyers.configurationservice.service.impl.InvitationServiceImpl;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
///**
// * Тесты для {@link InvitationService}
// */
//@ExtendWith(MockitoExtension.class)
//class InvitationServiceTest {
//
//    @Mock
//    private EmployeeService employeeService;
//
//    @Mock
//    private RemoteUserService remoteUserService;
//
//    @Mock
//    private InvitationRepository invitationRepository;
//
//    @Mock
//    private ConfigurationRepository configurationRepository;
//
//    @InjectMocks
//    private InvitationServiceImpl invitationService;
//
//    @Captor
//    private ArgumentCaptor<Invitation> invitationCaptor;
//
//    /**
//     * Тест добавления приглашения от несуществующей автомойки
//     */
//    @Test
//    void addInvitationForNotExistsConfiguration() {
//        InvitationRequest request = new InvitationRequest("test-user", 2L, Collections.emptyList());
//        Mockito.when(configurationRepository.findById(2L)).thenReturn(Optional.empty());
//
//        ConfigurationNotFoundException exception = Assertions.assertThrows(
//                ConfigurationNotFoundException.class, () -> invitationService.addInvitation(request)
//        );
//
//        Assertions.assertEquals("Configuration with id 2 not found", exception.getMessage());
//        Mockito.verify(invitationRepository, Mockito.never()).save(Mockito.any());
//    }
//
//    /**
//     * Тест добавления приглашения пользователю
//     */
//    @Test
//    void addInvitation() {
//        InvitationRequest invitationRequest = new InvitationRequest(
//                "test-user", 2L, List.of("ROLE_MANAGER")
//        );
//        Configuration configuration = Configuration.builder().id(2L).build();
//        Mockito.when(configurationRepository.findById(2L)).thenReturn(Optional.of(configuration));
//
//        invitationService.addInvitation(invitationRequest);
//
//        Mockito.verify(invitationRepository, Mockito.times(1)).save(invitationCaptor.capture());
//        Invitation invitation = invitationCaptor.getValue();
//        Assertions.assertEquals("test-user", invitation.getUsername());
//        Assertions.assertEquals(2L, invitation.getConfiguration().getId());
//        Assertions.assertEquals(InvitationState.SENT, invitation.getInvitationState());
//        Assertions.assertEquals(invitation.getRoles(), List.of("ROLE_MANAGER"));
//    }
//
//    /**
//     * Тест одобрения несуществующего приглашения
//     */
//    @Test
//    void applyNotExistsInvitation() {
//        Long invitationId = 1L;
//        Mockito.when(invitationRepository.findById(invitationId)).thenReturn(Optional.empty());
//
//        InvitationNotFoundException exception = Assertions.assertThrows(
//                InvitationNotFoundException.class, () -> invitationService.applyInvitation(invitationId)
//        );
//
//        Assertions.assertEquals("Invitation with id 1 not found", exception.getMessage());
//        Mockito.verify(employeeService, Mockito.never()).addCarWashEmployee(Mockito.any(), Mockito.any());
//        Mockito.verify(invitationRepository, Mockito.never()).save(Mockito.any());
//    }
//
//    /**
//     * Тест одобрения приглашения
//     */
//    @Test
//    void applyInvitation() {
//        Long invitationId = 1L;
//        Configuration configuration = Configuration.builder().id(1L).build();
//        Invitation invitation = Invitation.builder()
//                .id(invitationId)
//                .username("test-user")
//                .configuration(configuration)
//                .roles(List.of("ROLE_WASHER"))
//                .build();
//        UserDTO user = UserDTO.builder().id(2L).username("test-user").build();
//        Mockito.when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));
//        Mockito.when(remoteUserService.getUser("test-user")).thenReturn(user);
//
//        invitationService.applyInvitation(invitationId);
//
//        Mockito.verify(invitationRepository).save(invitationCaptor.capture());
//        Assertions.assertEquals(InvitationState.ACCEPTED, invitationCaptor.getValue().getInvitationState());
//        Mockito.verify(employeeService, Mockito.times(1)).addCarWashEmployee(configuration, 2L);
//    }
//
//    /**
//     * Тест отклонения несуществующего приглашения
//     */
//    @Test
//    void declineNotExistsInvitation() {
//        Long invitationId = 1L;
//        Mockito.when(invitationRepository.findById(invitationId)).thenReturn(Optional.empty());
//
//        InvitationNotFoundException exception = Assertions.assertThrows(
//                InvitationNotFoundException.class, () -> invitationService.declineInvitation(invitationId)
//        );
//
//        Assertions.assertEquals("Invitation with id 1 not found", exception.getMessage());
//        Mockito.verify(employeeService, Mockito.never()).addCarWashEmployee(Mockito.any(), Mockito.any());
//        Mockito.verify(invitationRepository, Mockito.never()).save(Mockito.any());
//    }
//
//    /**
//     * Тест отклонения приглашения
//     */
//    @Test
//    void declineInvitation() {
//        Long invitationId = 1L;
//        Invitation invitation = Invitation.builder()
//                .id(invitationId)
//                .username("test-user")
//                .roles(List.of("ROLE_WASHER"))
//                .build();
//        Mockito.when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));
//
//        invitationService.declineInvitation(invitationId);
//
//        Mockito.verify(invitationRepository).save(invitationCaptor.capture());
//        Assertions.assertEquals(InvitationState.REJECTED, invitationCaptor.getValue().getInvitationState());
//    }
//
//    /**
//     * Тест удаления приглашения
//     */
//    @Test
//    void deleteInvitation() {
//        invitationService.deleteInvitation(1L);
//        Mockito.verify(invitationRepository).deleteById(1L);
//    }
//
//}