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
     * Получить список идентификаторов пользователей, которым текущий пользователь отправлял сообщения
     *
     * @param userId идентификатор текущего пользователя
     */
    @Query("SELECT DISTINCT m.userId FROM ChatMessage m WHERE m.userId = :userId")
    Set<UUID> findByUserId(UUID userId);

    /**
     * Получить список идентификаторов пользователей, от которых были отправлены сообщения
     *
     * @param userId идентификатор пользователя, которому отправлялись сообщения
     */
    @Query("SELECT DISTINCT m.userId FROM ChatMessage m WHERE m.targetId = :userId")
    Set<UUID> findTargetsByUserId(UUID userId);

}
