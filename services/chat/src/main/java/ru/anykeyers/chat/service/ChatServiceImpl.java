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

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    private final RemoteUserService remoteUserService;

    private final SimpMessagingTemplate messagingTemplate;

    public Set<User> getUserChats(User user) {
        return remoteUserService.getUsers(
                chatRepository.findByUserId(user.getId())
        );
    }

    public Set<User> getCarWashOwnerChats(User user) {
        return remoteUserService.getUsers(
                chatRepository.findTargetsByUserId(user.getId())
        );
    }

    public List<ChatMessage> getChatMessages(User user, UUID targetId) {
        List<ChatMessage> messages = chatRepository.findByUserIdAndTargetId(user.getId(), targetId);
        processMessagesStatus(targetId, messages);
        return messages;
    }

    public void sendMessage(User user, ChatMessage message) {
        message.setUserId(user.getId());
        message.setStatus(ChatMessage.Status.DELIVERED);
        chatRepository.save(message);
        messagingTemplate.convertAndSendToUser(message.getTargetId().toString(), "/queue/messages", message);
    }

    private void processMessagesStatus(UUID senderId, List<ChatMessage> messages) {
        messages.forEach(message -> processMessageStatus(senderId, message));
    }

    private void processMessageStatus(UUID senderId, ChatMessage message) {
        if (!message.getUserId().equals(senderId) || message.getStatus().equals(ChatMessage.Status.SEEN)) {
            return;
        }
        message.setStatus(ChatMessage.Status.SEEN);
    }

}
