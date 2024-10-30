package ru.anykeyers.chat.web;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.anykeyers.chat.domain.ChatMessage;
import ru.anykeyers.chat.service.ChatService;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping(ControllerName.BASE_URL + "/sendMessage/{convId}")
    @SendTo("/topic/outgoing")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage,
                                   SimpMessagingTemplate headerAccessor,
                                   @DestinationVariable String conversationId) {
        return chatService.sendMessage(chatMessage, conversationId);
    }

}
