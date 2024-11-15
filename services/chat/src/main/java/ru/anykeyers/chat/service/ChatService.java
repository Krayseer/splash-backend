package ru.anykeyers.chat.service;

import ru.anykeyers.chat.domain.ChatMessage;
import ru.anykeyers.commonsapi.domain.user.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Сервис обработки чатов
 */
public interface ChatService {

    /**
     * Получить список пользователей, которым были отправлены сообщения
     *
     * @param user пользователь, который отправлял сообщения
     */
    Set<User> getUserChats(User user);

    /**
     * Получить список пользователей, которые отправляли сообщения владельцу автомойки
     *
     * @param user владелец автомойки
     */
    Set<User> getCarWashOwnerChats(User user);

    /**
     * Получить список сообщений чата
     *
     * @param user      пользователь
     * @param targetId  идентификатор получателя
     */
    List<ChatMessage> getChatMessages(User user, UUID targetId);

    /**
     * Отправить сообщение
     *
     * @param user      отправитель
     * @param message   сообщение
     */
    void sendMessage(User user, ChatMessage message);

}
