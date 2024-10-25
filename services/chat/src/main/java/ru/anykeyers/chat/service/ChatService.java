package ru.anykeyers.chat.service;

import org.springframework.stereotype.Service;
import ru.anykeyers.chat.domain.ChatMessage;

/**
 * Сервис обработки чатов
 */
@Service
public class ChatService {

    /**
     * Отправить сообщение
     *
     * @param chatMessage       сообщение
     * @param conversationId    идентификатор чата
     */
    public ChatMessage sendMessage(ChatMessage chatMessage, String conversationId) {
        return null;
    }

}
