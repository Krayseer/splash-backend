package ru.anykeyers.configurationservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.service.ConfigurationService;
import ru.anykeyers.configurationservice.web.dto.InvitationDTO;
import ru.anykeyers.configurationservice.domain.invitation.InvitationState;
import ru.anykeyers.configurationservice.domain.invitation.Invitation;
import ru.anykeyers.configurationservice.exception.InvitationNotFoundException;
import ru.anykeyers.configurationservice.repository.InvitationRepository;
import ru.anykeyers.configurationservice.service.EmployeeService;
import ru.anykeyers.configurationservice.service.InvitationService;

import java.util.List;

/**
 * Реализация сервиса обработки приглашений
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final EmployeeService employeeService;

    private final InvitationRepository invitationRepository;

    private final ConfigurationService configurationService;

    @Override
    public List<Invitation> getInvitations(User user) {
        return invitationRepository.findByUserId(user.getId());
    }

    @Override
    public List<Invitation> getInvitations(Long carWashId) {
        return invitationRepository.findByConfigurationId(carWashId);
    }

    @Override
    public List<Invitation> getHolderInvitations(User user) {
        Configuration configuration = configurationService.getConfiguration(user);
        return getInvitations(configuration.getId());
    }

    @Override
    public List<Invitation> getInvitations(Long carWashId, InvitationState invitationState) {
        return invitationRepository.findByConfigurationIdAndInvitationState(carWashId, invitationState);
    }

    @Override
    public void addInvitation(InvitationDTO invitationDTO) {
        Invitation invitation = Invitation.builder()
                .userId(invitationDTO.getUser().getId())
                .invitationState(InvitationState.SENT)
                .roles(invitationDTO.getRoles())
                .configuration(configurationService.getConfiguration(invitationDTO.getCarWashId()))
                .build();
        invitationRepository.save(invitation);
        log.info("Send invitation: {}", invitation);
    }

    @Override
    @SneakyThrows
    public void applyInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId).orElseThrow(
                () -> new InvitationNotFoundException(invitationId)
        );
        invitation.setInvitationState(InvitationState.ACCEPTED);
        employeeService.addCarWashEmployee(invitation.getConfiguration(), invitation.getUserId());
        invitationRepository.save(invitation);
        log.info("Apply invitation: {}", invitation);
    }

    @Override
    public void declineInvitation(Long id) {
        Invitation invitation = invitationRepository.findById(id).orElseThrow(
                () -> new InvitationNotFoundException(id)
        );
        invitation.setInvitationState(InvitationState.REJECTED);
        invitationRepository.save(invitation);
        log.info("Decline invitation: {}", invitation);
    }

    @Override
    public void deleteInvitation(Long id) {
        invitationRepository.deleteById(id);
        log.info("Delete invitation: {}", id);
    }

}
