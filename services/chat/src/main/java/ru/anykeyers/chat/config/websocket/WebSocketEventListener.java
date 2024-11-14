package ru.anykeyers.chat.config.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.utils.JwtUtils;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        String simpDestination = (String) sessionSubscribeEvent.getMessage().getHeaders().get("simpDestination");
        User user = JwtUtils.extractUser(sessionSubscribeEvent.getUser());
        UUID userId = user.getId();
        log.info("User {} subscribed to personal channel: {}", userId, simpDestination);
    }

}
