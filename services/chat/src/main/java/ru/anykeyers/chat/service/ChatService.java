package ru.anykeyers.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.anykeyers.chat.domain.ChatMessage;
import ru.anykeyers.chat.repository.ChatRepository;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.remote.RemoteUserService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Сервис обработки чатов
 */
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    private final SimpMessagingTemplate messagingTemplate;

    private final RemoteUserService remoteUserService;

    /**
     * Получить список пользователей, которые отправили сообщения
     *
     * @param user текущий пользователь
     */
    public Set<User> getChats(User user) {
        Set<UUID> senderIds = chatRepository.findAllContactUsers(user.getId());
        return remoteUserService.getUsers(senderIds);
    }

    /**
     * Получить список пользователей, которые отправили сообщения
     *
     * @param user текущий пользователь
     */
    public Set<UUID> getChatsIds(User user) {
        return chatRepository.findAllContactUsers(user.getId());
    }

    /**
     * Получить список сообщений чата
     *
     * @param user      пользователь
     * @param senderId  идентификатор отправителя
     */
    public List<ChatMessage> getChatMessages(User user, UUID senderId) {
        return chatRepository.findByReceiverIdAndSenderId(user.getId(), senderId);
    }

    /**
     * Отправить сообщение
     *
     * @param sender    отправитель
     * @param message   сообщение
     */
    public void sendMessage(User sender, ChatMessage message) {
        UUID receiverId = message.getReceiverId();
        message.setSenderId(sender.getId());
        message.setStatus(ChatMessage.Status.DELIVERED);
        chatRepository.save(message);
        messagingTemplate.convertAndSendToUser(receiverId.toString(), "/queue/messages", message);
    }

    private void processMessagesStatus(List<ChatMessage> messages) {
        messages.forEach(message -> {
            if (message.getStatus().equals(ChatMessage.Status.DELIVERED)) {
                message.setStatus(ChatMessage.Status.SEEN);
            }
        });
    }

}
