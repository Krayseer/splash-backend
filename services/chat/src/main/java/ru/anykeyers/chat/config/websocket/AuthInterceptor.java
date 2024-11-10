package ru.anykeyers.chat.config.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements ChannelInterceptor {

    private static final int BEARER_TOKEN_START_INDEX = 7;

    private static final String AUTH_HEADER = "Authorization";

    private final JwtDecoder jwtDecoder;

    @Override
    public Message<?> preSend(@Nonnull Message<?> message, @Nonnull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || accessor.getUser() != null) {
            return message;
        }
        String authHeader = accessor.getFirstNativeHeader(AUTH_HEADER);
        if (authHeader == null) {
            return message;
        }
        String token = authHeader.substring(BEARER_TOKEN_START_INDEX);
        Jwt jwt = jwtDecoder.decode(token);
        accessor.setUser(new JwtAuthenticationToken(jwt));
        return message;
    }

}
