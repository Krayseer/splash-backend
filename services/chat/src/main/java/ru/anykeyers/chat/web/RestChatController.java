package ru.anykeyers.chat.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anykeyers.chat.domain.ChatMessage;
import ru.anykeyers.chat.service.ChatService;
import ru.anykeyers.chat.web.dto.ChatDTO;
import ru.anykeyers.chat.web.dto.UserChatDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;
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
@Tag(name = "Получение сообщений и чатов")
public class RestChatController {

    private final ChatService chatService;

    private final RemoteConfigurationService remoteConfigurationService;

    @Operation(summary = "Получить список чатов пользователя")
    @GetMapping("/chats")
    public Set<UserChatDTO> getUserChats(Principal principal) {
        User user = JwtUtils.extractUser(principal);
        Set<User> targetUsers = chatService.getUserChats(user);
        return targetUsers.stream()
                .map(target -> new UserChatDTO(chatService.getLastMessage(user, target), remoteConfigurationService.getUserConfiguration(target.getId())))
                .collect(Collectors.toSet());
    }

    @Operation(summary = "Получить список чатов владельца автомойки")
    @GetMapping
    public Set<ChatDTO> getCarWashOwnerChats(Principal principal) {
        User user = JwtUtils.extractUser(principal);
        return getChats(user, () -> chatService.getCarWashOwnerChats(user));
    }

    @Operation(summary = "Получить список сообщений с другим пользователем")
    @GetMapping("/messages/{senderId}")
    public List<ChatMessage> getChatMessages(
            @Parameter(description = "Идентификатор другого пользователя") @PathVariable UUID senderId,
            Principal principal
    ) {
        return chatService.getChatMessages(JwtUtils.extractUser(principal), senderId);
    }

    private Set<ChatDTO> getChats(User user, Supplier<Set<User>> targetUsersSupplier) {
        Set<User> targetUsers = targetUsersSupplier.get();
        return targetUsers.stream()
                .map(target -> new ChatDTO(chatService.getLastMessage(user, target), target))
                .collect(Collectors.toSet());
    }

}
