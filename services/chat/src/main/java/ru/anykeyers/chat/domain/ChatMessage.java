package ru.anykeyers.chat.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Сообщение
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    /**
     * Идентификатор сообщения
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор отправителя
     */
    private UUID userId;

    /**
     * Идентификатор получателя
     */
    private UUID targetId;

    /**
     * Текст сообщения
     */
    private String content;

    /**
     * Статус сообщения
     */
    private Status status;

    /**
     * Время создания сообщения
     */
    @CreationTimestamp
    private Instant createdAt;

    /**
     * Статус
     */
    public enum Status {
        /**
         * Доставлен
         */
        DELIVERED,
        /**
         * Прочитан
         */
        SEEN
    }

}
