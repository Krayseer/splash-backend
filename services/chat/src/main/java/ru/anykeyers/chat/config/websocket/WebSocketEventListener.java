package ru.anykeyers.chat.config.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;
import ru.anykeyers.chat.service.ChatService;
import ru.anykeyers.chat.service.UserService;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.utils.JwtUtils;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final UserService userService;

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatService chatService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        userService.addOnlineUser(JwtUtils.extractUser(event.getUser()));
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        userService.removeOnlineUser(JwtUtils.extractUser(event.getUser()));
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        String simpDestination = (String) sessionSubscribeEvent.getMessage().getHeaders().get("simpDestination");
        User user = JwtUtils.extractUser(sessionSubscribeEvent.getUser());
        UUID userId = user.getId();
        messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/messages", chatService.getChatsIds(user));
        log.info("User {} subscribed to personal channel: {}", userId, simpDestination);
    }

}
