package ru.anykeyers.configurationservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.commonsapi.utils.JwtUtils;
import ru.anykeyers.configurationservice.web.dto.InvitationDTO;
import ru.anykeyers.configurationservice.domain.invitation.InvitationState;
import ru.anykeyers.configurationservice.service.InvitationService;
import ru.anykeyers.configurationservice.web.ControllerName;
import ru.anykeyers.configurationservice.web.mapper.InvitationMapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.INVITATION_CONTROLLER)
@Tag(name = "Обработка отправки приглашений")
public class InvitationController {

    private final InvitationMapper invitationMapper;

    private final InvitationService invitationService;

    @Operation(summary = "Получить список приглашений авторизованного пользователя")
    @GetMapping
    public List<InvitationDTO> getInvitations(@AuthenticationPrincipal Jwt jwt) {
        return invitationMapper.toDTO(
                invitationService.getInvitations(JwtUtils.extractUser(jwt))
        );
    }

    @Operation(summary = "Получить список приглашей, отправленных от лица владельца автомойки")
    @GetMapping("/by-holder")
    public List<InvitationDTO> getHolderInvitations(@AuthenticationPrincipal Jwt jwt) {
        return invitationMapper.toDTO(
                invitationService.getHolderInvitations(JwtUtils.extractUser(jwt))
        );
    }

    @Operation(summary = "Получить список приглашений, отправленных от лица автомойки")
    @GetMapping("/{carWashId}")
    public List<InvitationDTO> getInvitation(
            @Parameter(description = "Идентификатор автомойки") @PathVariable Long carWashId
    ) {
        return invitationMapper.toDTO(
                invitationService.getInvitations(carWashId)
        );
    }

    @Operation(summary = "Получить список отправленных не обработанных приглашений автомойки")
    @GetMapping("/{carWashId}/sent")
    public List<InvitationDTO> getSentInvitations(
            @Parameter(description = "Идентификатор автомойки") @PathVariable Long carWashId
    ) {
        return invitationMapper.toDTO(
                invitationService.getInvitations(carWashId, InvitationState.SENT)
        );
    }

    @Operation(summary = "Получить список принятых приглашений автомойки")
    @GetMapping("/{carWashId}/accepted")
    public List<InvitationDTO> getAcceptedInvitations(
            @Parameter(description = "Идентификатор автомойки") @PathVariable Long carWashId
    ) {
        return invitationMapper.toDTO(
                invitationService.getInvitations(carWashId, InvitationState.ACCEPTED)
        );
    }

    @Operation(summary = "Получить список отклоненных приглашений автомойки")
    @GetMapping("/{carWashId}/rejected")
    public List<InvitationDTO> getRejectedInvitations(
            @Parameter(description = "Идентификатор автомойки") @PathVariable Long carWashId
    ) {
        return invitationMapper.toDTO(
                invitationService.getInvitations(carWashId, InvitationState.REJECTED)
        );
    }

    @Operation(summary = "Добавление данных об отправленном приглашении")
    @PostMapping
    public void addInvitation(@RequestBody InvitationDTO invitation) {
        invitationService.addInvitation(invitation);
    }

    @Operation(summary = "Подтверждение приглашения")
    @PostMapping("/apply/{id}")
    public void applyInvitation(
            @Parameter(description = "Идентификатор приглашения") @PathVariable Long id
    ) {
        invitationService.applyInvitation(id);
    }

    @Operation(summary = "Отклонить приглашение")
    @PostMapping("/decline/{id}")
    public void declineInvitation(
            @Parameter(description = "Идентификатор приглашения") @PathVariable Long id
    ) {
        invitationService.declineInvitation(id);
    }

    @Operation(summary = "Удалить приглашение")
    @DeleteMapping("/{id}")
    public void deleteInvitation(
            @Parameter(description = "Идентификатор приглашения") @PathVariable Long id
    ) {
        invitationService.deleteInvitation(id);
    }

}
