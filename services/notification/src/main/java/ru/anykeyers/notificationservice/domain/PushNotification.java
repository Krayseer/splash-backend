package ru.anykeyers.notificationservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Push уведомление
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushNotification {

    /**
     * Идентификатор
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя
     */
    @Column(name = "USERNAME")
    private String username;

    /**
     * Тема
     */
    @Column(name = "SUBJECT")
    private String subject;

    /**
     * Сообщение
     */
    @Column(name = "MESSAGE")
    private String message;

    /**
     * Время создания
     */
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;

}
