package ru.anykeyers.chat.web;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anykeyers.chat.domain.ChatMessage;
import ru.anykeyers.chat.service.ChatService;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.BASE_URL)
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/sendMessage/{convId}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage,
                                   SimpMessagingTemplate headerAccessor,
                                   @DestinationVariable String conversationId) {
        return chatService.sendMessage(chatMessage, conversationId);
    }

}
