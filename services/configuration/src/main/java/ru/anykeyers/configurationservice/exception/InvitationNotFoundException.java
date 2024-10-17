package ru.anykeyers.configurationservice.exception;

/**
 * Исключение, сигнализирующее, что приглашения не существует
 */
public class InvitationNotFoundException extends RuntimeException {

    public InvitationNotFoundException(Long invitationId) {
        super(String.format("Invitation with id %s not found", invitationId));
    }

}
