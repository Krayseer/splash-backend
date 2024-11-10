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
     * @param receiverId    идентификатор получателя
     * @param senderId      идентификатор отправителя
     */
    List<ChatMessage> findByReceiverIdAndSenderId(UUID receiverId, UUID senderId);

    /**
     * Получить список всех чатов, с которым есть взаимодействие у пользователя
     *
     * @param userId идентификатор пользователя
     */
    @Query("SELECT DISTINCT m.senderId FROM ChatMessage m WHERE m.receiverId = :userId " +
            "UNION " +
            "SELECT DISTINCT m.receiverId FROM ChatMessage m WHERE m.senderId = :userId")
    Set<UUID> findAllContactUsers(UUID userId);

}
