package ru.anykeyers.chat.domain;

import lombok.*;

import java.util.UUID;

/**
 * Сообщение
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    /**
     * Идентификатор сообщения
     */
    private UUID id;

    /**
     * Идентификатор отправителя
     */
    private UUID sender;

    /**
     * Идентификатор получателя
     */
    private UUID receiver;

    /**
     * Текст сообщения
     */
    private String content;

    /**
     * Тип сообщения
     */
    private Type type;

    /**
     * Статус сообщения
     */
    private Status status;

    public enum Type {
        CHAT,
        UNSEEN,
        FRIEND_ONLINE,
        FRIEND_OFFLINE,
        MESSAGE_DELIVERY_UPDATE
    }

    public enum Status {
        NOT_DELIVERED,
        DELIVERED,
        SEEN
    }

}
