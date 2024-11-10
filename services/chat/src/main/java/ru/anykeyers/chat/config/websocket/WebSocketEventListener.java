package ru.anykeyers.chat.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;
import ru.anykeyers.chat.service.SubscriptionService;
import ru.anykeyers.chat.service.UserService;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.utils.JwtUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketEventListener {

    private final UserService userService;

    private final SubscriptionService subscriptionService;

    private final Map<String, UUID> simpSessionIdToSubscriptionId;

    private final JwtDecoder jwtDecoder;

    @Autowired
    public WebSocketEventListener(UserService userService,
                                  JwtDecoder jwtDecoder) {
        this.userService = userService;
        this.subscriptionService = new SubscriptionService();
        this.simpSessionIdToSubscriptionId = new ConcurrentHashMap<>();
        this.jwtDecoder = jwtDecoder;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        jwtDecoder.decode("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxNDA1MDM4NCwiZXhwIjoxNzE0MDUwOTg0fQ.0iREafAqZx-H41QU1xQbM5td-0YjYgUjwByeiD0-79w");
        userService.addOnlineUser(JwtUtils.extractUser(event.getUser()));
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        userService.removeOnlineUser(JwtUtils.extractUser(event.getUser()));
    }

    @EventListener
    @SendToUser
    public void handleSubscribeEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        UUID subscribedChannel =
                (UUID) sessionSubscribeEvent.getMessage().getHeaders().get("simpDestination");
        String simpSessionId =
                (String) sessionSubscribeEvent.getMessage().getHeaders().get("simpSessionId");
        if (subscribedChannel == null) {
            log.error("Subscribed to null channel");
            return;
        }
        simpSessionIdToSubscriptionId.put(simpSessionId, subscribedChannel);
        User user = JwtUtils.extractUser(sessionSubscribeEvent.getUser());
        subscriptionService.subscribe(user.getId(), subscribedChannel);
    }

    @EventListener
    public void handleUnSubscribeEvent(SessionUnsubscribeEvent unsubscribeEvent) {
        String simpSessionId = (String) unsubscribeEvent.getMessage().getHeaders().get("simpSessionId");
        UUID unSubscribedChannel = simpSessionIdToSubscriptionId.get(simpSessionId);
        User user = JwtUtils.extractUser(unsubscribeEvent.getUser());
        subscriptionService.unsubscribe(user.getId(), unSubscribedChannel);
    }

}
