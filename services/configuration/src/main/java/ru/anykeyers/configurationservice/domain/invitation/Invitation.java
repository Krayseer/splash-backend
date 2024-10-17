package ru.anykeyers.configurationservice.domain.invitation;

import jakarta.persistence.*;
import lombok.*;
import ru.anykeyers.configurationservice.domain.Configuration;

import java.util.List;
import java.util.UUID;

/**
 * Приглашение
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор пользователя, кому отправлено приглашение
     */
    private UUID userId;

    /**
     * Идентификатор автомойки
     */
    @ManyToOne
    @JoinColumn(name = "CONFIGURATION_ID")
    private Configuration configuration;

    /**
     * Список ролей
     */
    @ElementCollection
    private List<String> roles;

    /**
     * Состояние приглашения
     */
    @Enumerated(EnumType.STRING)
    private InvitationState invitationState;

}
