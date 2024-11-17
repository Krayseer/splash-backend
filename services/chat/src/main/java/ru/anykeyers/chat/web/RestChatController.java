package ru.anykeyers.chat.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anykeyers.chat.domain.ChatMessage;
import ru.anykeyers.chat.service.ChatService;
import ru.anykeyers.chat.web.dto.ChatDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.utils.JwtUtils;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ControllerName.BASE_URL)
@RequiredArgsConstructor
public class RestChatController {

    private final ChatService chatService;

    @GetMapping("/chats")
    public Set<ChatDTO> getUserChats(Principal principal) {
        User user = JwtUtils.extractUser(principal);
        return getChats(user, () -> chatService.getUserChats(user));
    }

    @GetMapping
    public Set<ChatDTO> getCarWashOwnerChats(Principal principal) {
        User user = JwtUtils.extractUser(principal);
        return getChats(user, () -> chatService.getCarWashOwnerChats(user));
    }

    @GetMapping("/messages/{senderId}")
    public List<ChatMessage> getChatMessages(@PathVariable UUID senderId, Principal principal) {
        return chatService.getChatMessages(JwtUtils.extractUser(principal), senderId);
    }

    private Set<ChatDTO> getChats(User user, Supplier<Set<User>> targetUsersSupplier) {
        Set<User> targetUsers = targetUsersSupplier.get();
        return targetUsers.stream()
                .map(target -> new ChatDTO(chatService.getLastMessage(user, target), user))
                .collect(Collectors.toSet());
    }

}
