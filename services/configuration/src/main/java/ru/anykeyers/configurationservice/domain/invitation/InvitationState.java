package ru.anykeyers.configurationservice.domain.invitation;

/**
 * Состояние приглашения
 */
public enum InvitationState {
    /**
     * Отправлен
     */
    SENT,
    /**
     * Одобрен
     */
    ACCEPTED,
    /**
     * Отклонен
     */
    REJECTED
}
