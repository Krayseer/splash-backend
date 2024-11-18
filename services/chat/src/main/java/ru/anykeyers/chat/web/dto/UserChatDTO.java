package ru.anykeyers.chat.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.anykeyers.chat.domain.ChatMessage;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;

/**
 * Чат для пользователя
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserChatDTO {

    /**
     * Последнее сообщение
     */
    private ChatMessage lastMessage;

    /**
     * Автомойка, с которой существует чат
     */
    private ConfigurationDTO carWash;


}
