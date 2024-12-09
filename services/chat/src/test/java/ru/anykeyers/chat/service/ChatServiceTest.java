package ru.anykeyers.chat.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.anykeyers.chat.domain.ChatMessage;
import ru.anykeyers.chat.repository.ChatRepository;
import ru.anykeyers.commonsapi.domain.user.User;

import java.time.Instant;
import java.util.UUID;

/**
 * Тесты для {@link ChatService}
 */
@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private ChatServiceImpl chatService;

    @Captor
    private ArgumentCaptor<ChatMessage> captor;

    private final UUID userId = UUID.randomUUID();

    private final User user = User.builder().id(userId).build();

    private final UUID targetId = UUID.randomUUID();

    /**
     * Тест отправки сообщения пользователю
     */
    @Test
    void sendMessage() {
        // Подготовка
        Instant now = Instant.now();
        ChatMessage message = ChatMessage.builder()
                .userId(userId)
                .targetId(targetId)
                .content("Привет!")
                .createdAt(now)
                .build();

        // Действие
        chatService.sendMessage(user, message);

        // Проверка
        Mockito.verify(chatRepository, Mockito.times(1)).save(captor.capture());
        ChatMessage captured = captor.getValue();

        Assertions.assertEquals(userId, captured.getUserId());
        Assertions.assertEquals(targetId, captured.getTargetId());
        Assertions.assertEquals("Привет!", captured.getContent());
        Assertions.assertEquals(now, captured.getCreatedAt());
        Assertions.assertEquals(ChatMessage.Status.DELIVERED, captured.getStatus());

        Mockito.verify(simpMessagingTemplate).convertAndSendToUser(
                targetId.toString(), "/queue/messages", message
        );
    }

}