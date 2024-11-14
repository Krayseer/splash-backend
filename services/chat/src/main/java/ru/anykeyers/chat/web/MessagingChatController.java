package ru.anykeyers.chat.web;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.anykeyers.chat.domain.ChatMessage;
import ru.anykeyers.chat.service.ChatService;
import ru.anykeyers.commonsapi.utils.JwtUtils;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessagingChatController {

    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(Message<ChatMessage> message, Principal userPrincipal) {
        chatService.sendMessage(JwtUtils.extractUser(userPrincipal), message.getPayload());
    }

}
