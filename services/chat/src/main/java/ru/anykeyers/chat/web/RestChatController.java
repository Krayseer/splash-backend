package ru.anykeyers.chat.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anykeyers.chat.domain.ChatMessage;
import ru.anykeyers.chat.service.ChatService;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.utils.JwtUtils;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(ControllerName.BASE_URL)
@RequiredArgsConstructor
public class RestChatController {

    private final ChatService chatService;

    @GetMapping("/chats")
    public Set<User> getChats(Principal principal) {
        return chatService.getChats(JwtUtils.extractUser(principal));
    }

    @GetMapping("/messages/{senderId}")
    public List<ChatMessage> getChatMessages(@PathVariable UUID senderId, Principal principal) {
        return chatService.getChatMessages(JwtUtils.extractUser(principal), senderId);
    }

}
