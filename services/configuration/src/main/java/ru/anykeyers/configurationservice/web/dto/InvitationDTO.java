package ru.anykeyers.configurationservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.anykeyers.configurationservice.domain.invitation.InvitationState;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationDTO {
    private UUID userId;
    private Long carWashId;
    private List<String> roles;

    private Long id;
    private InvitationState invitationState;
}
