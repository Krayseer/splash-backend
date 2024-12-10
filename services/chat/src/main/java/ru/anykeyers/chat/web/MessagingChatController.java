package ru.anykeyers.chat.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Обработка отправки сообщений")
public class MessagingChatController {

    private final ChatService chatService;

    @Operation(summary = "Отправить сообщение пользователю")
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(
            @Parameter(description = "Сообщение") Message<ChatMessage> message,
            Principal userPrincipal
    ) {
        chatService.sendMessage(JwtUtils.extractUser(userPrincipal), message.getPayload());
    }

}
