package ru.anykeyers.commonsapi.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Пользователь (используется по факту как DTO)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * Идентификатор
     */
    private UUID id;
    /**
     * Имя пользователя
     */
    private String username;
    /**
     * Пароль
     */
    @JsonProperty(
            access = JsonProperty.Access.WRITE_ONLY
    )
    private String password;
    /**
     * Данные пользователя
     */
    private UserInfo userInfo;
    /**
     * Список ролей
     */
    private List<String> roles;
    /**
     * Дата создания
     */
    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    private long createdTimestamp;
    /**
     * Настройки пользователя
     */
    private Setting setting;

    /**
     * Настройки
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Setting {
        /**
         * Разрешена отправка push уведомлений
         */
        private boolean pushEnabled;
        /**
         * Разрешена отправка email уведомлений
         */
        private boolean emailEnabled;
    }

}
