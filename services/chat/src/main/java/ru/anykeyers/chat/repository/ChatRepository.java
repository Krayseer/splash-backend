package ru.anykeyers.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.anykeyers.chat.domain.ChatMessage;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * DAO для работы с сообщениями
 */
@Repository
public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * Получить список всех сообщений конкретного чата
     *
     * @param userId    идентификатор получателя
     * @param targetId  идентификатор отправителя
     */
    List<ChatMessage> findByUserIdAndTargetId(UUID userId, UUID targetId);

    /**
     * Получить список всех чатов, с которым есть взаимодействие у пользователя
     *
     * @param userId идентификатор пользователя
     */
    @Query("SELECT DISTINCT m.userId FROM ChatMessage m WHERE m.targetId = :userId " +
            "UNION " +
            "SELECT DISTINCT m.targetId FROM ChatMessage m WHERE m.userId = :userId")
    Set<UUID> findAllContactUsers(UUID userId);


    @Query("SELECT DISTINCT m.userId FROM ChatMessage m WHERE m.userId = :userId")
    Set<UUID> findByUserId(UUID userId);

    @Query("SELECT DISTINCT m.targetId FROM ChatMessage m WHERE m.userId = :userId")
    Set<UUID> findTargetsByUserId(UUID userId);

}
