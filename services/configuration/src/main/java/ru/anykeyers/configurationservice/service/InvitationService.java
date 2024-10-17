package ru.anykeyers.configurationservice.service;

import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.configurationservice.web.dto.InvitationDTO;
import ru.anykeyers.configurationservice.domain.invitation.InvitationState;
import ru.anykeyers.configurationservice.domain.invitation.Invitation;

import java.util.List;

/**
 * Сервис обработки приглашений
 */
public interface InvitationService {

    /**
     * Получить список приглашений пользователя
     *
     * @param user пользователь
     */
    List<Invitation> getInvitations(User user);

    /**
     * Получить список приглашений автомойки
     *
     * @param carWashId идентификатор автомойки
     */
    List<Invitation> getInvitations(Long carWashId);

    /**
     * Получить список приглашений автомойки
     *
     * @param carWashId         идентификатор автомойки
     * @param invitationState   статус приглашения
     */
    List<Invitation> getInvitations(Long carWashId, InvitationState invitationState);

    /**
     * Добавить приглашение
     *
     * @param invitationDTO данные о приглашении
     */
    void addInvitation(InvitationDTO invitationDTO);

    /**
     * Одобрить приглашение
     *
     * @param invitationId идентификатор приглашения
     */
    void applyInvitation(Long invitationId);

    /**
     * Отклонить приглашение
     *
     * @param id идентификатор приглашения
     */
    void declineInvitation(Long id);

    /**
     * Удалить приглашение
     *
     * @param id идентификатор приглашения
     */
    void deleteInvitation(Long id);

}
