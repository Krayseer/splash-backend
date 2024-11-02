package ru.anykeyers.configurationservice.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    private Long id;

    private UUID userId;

    private Long carWashId;

    private List<String> roles;

    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    private InvitationState invitationState;

}
