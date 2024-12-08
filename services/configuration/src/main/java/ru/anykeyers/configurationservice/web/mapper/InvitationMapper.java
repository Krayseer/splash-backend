package ru.anykeyers.configurationservice.web.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.remote.RemoteUserService;
import ru.anykeyers.configurationservice.domain.invitation.Invitation;
import ru.anykeyers.configurationservice.web.dto.InvitationDTO;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InvitationMapper {

    private final ModelMapper modelMapper;

    private final RemoteUserService remoteUserService;

    public InvitationDTO toDTO(Invitation invitation) {
        InvitationDTO invitationDTO = modelMapper.map(invitation, InvitationDTO.class);
        invitationDTO.setUser(remoteUserService.getUser(invitation.getUserId()));
        invitationDTO.setCarWashId(invitation.getConfiguration().getId());
        return invitationDTO;
    }

    public List<InvitationDTO> toDTO(List<Invitation> invitations) {
        return invitations.stream().map(this::toDTO).toList();
    }

}
