package ru.anykeyers.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import ru.anykeyers.chat.domain.ChatMessage;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.remote.RemoteUserService;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Сервис обработки пользователей
 */
@Slf4j
@Service
public class UserService {

    private final Set<UUID> onlineUsers;

    private final RemoteUserService remoteUserService;

    private final SubscriptionService subscriptionService;

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    public UserService(RemoteUserService remoteUserService,
                       SimpMessageSendingOperations simpMessageSendingOperations) {
        this.onlineUsers = new ConcurrentSkipListSet<>();
        this.remoteUserService = remoteUserService;
        this.subscriptionService = new SubscriptionService();
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    public void addOnlineUser(User user) {
        if (user == null) {
            return;
        }
        log.info("{} is online", user.getUsername());
        subscriptionService.addSubscription(user.getId());
        for (UUID id : onlineUsers) {
            simpMessageSendingOperations.convertAndSend(
                    "/topic/" + id,
                    ChatMessage.builder()
                            .build());
        }
        onlineUsers.add(user.getId());
    }

    public void removeOnlineUser(User user) {
        if (user == null) {
            return;
        }
        log.info("{} went offline", user.getUsername());
        onlineUsers.remove(user.getId());
        subscriptionService.removeSubscription(user.getId());
        for (UUID id : onlineUsers) {
            simpMessageSendingOperations.convertAndSend(
                    "/topic/" + id,
                    ChatMessage.builder()
                            .build());
        }
    }

    public boolean isUserOnline(UUID userId) {
        return onlineUsers.contains(userId);
    }

    public List<User> getOnlineUsers() {
        return new ArrayList<>(remoteUserService.getUsers(onlineUsers));
    }

//    public void notifySender(UUID senderId, List<ConversationEntity> entities, MessageDeliveryStatusEnum deliveryStatus) {
//        if (!isUserOnline(senderId)) {
//            log.info(
//                    "{} is not online. cannot inform the socket. will persist in database",
//                    senderId.toString());
//            return;
//        }
//        List<MessageDeliveryStatusUpdate> messageDeliveryStatusUpdates =
//                entities.stream()
//                        .map(
//                                e ->
//                                        MessageDeliveryStatusUpdate.builder()
//                                                .id(e.getId())
//                                                .messageDeliveryStatusEnum(deliveryStatus)
//                                                .content(e.getContent())
//                                                .build())
//                        .toList();
//        for (ConversationEntity entity : entities) {
//            simpMessageSendingOperations.convertAndSend(
//                    "/topic/" + senderId,
//                    ChatMessage.builder()
//                            .id(entity.getId())
//                            .messageDeliveryStatusUpdates(messageDeliveryStatusUpdates)
//                            .messageType(MessageType.MESSAGE_DELIVERY_UPDATE)
//                            .content(entity.getContent())
//                            .build());
//        }
//    }

}
