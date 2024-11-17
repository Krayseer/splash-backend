package ru.anykeyers.chat.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.anykeyers.chat.domain.ChatMessage;
import ru.anykeyers.commonsapi.domain.user.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {

    /**
     * Последнее сообщение
     */
    private ChatMessage lastMessage;

    /**
     * Пользователь, с которым существует чат
     */
    private User user;

}
