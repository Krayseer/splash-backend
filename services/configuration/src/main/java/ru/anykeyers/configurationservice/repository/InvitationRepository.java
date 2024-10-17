package ru.anykeyers.configurationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anykeyers.configurationservice.domain.invitation.InvitationState;
import ru.anykeyers.configurationservice.domain.invitation.Invitation;

import java.util.List;
import java.util.UUID;

/**
 * DAO для работы с приглашениями
 */
@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    /**
     * Получить список приглашений пользователя
     *
     * @param userId идентификатор пользователя
     */
    List<Invitation> findByUserId(UUID userId);

    /**
     * Получить список приглашений автомойки
     *
     * @param carWashId идентификатор автомойки
     */
    List<Invitation> findByConfigurationId(Long carWashId);

    /**
     * Получить список приглашений автомойки
     *
     * @param carWashId         идентификатор автомойки
     * @param invitationState   статус приглашения
     */
    List<Invitation> findByConfigurationIdAndInvitationState(Long carWashId, InvitationState invitationState);

}
