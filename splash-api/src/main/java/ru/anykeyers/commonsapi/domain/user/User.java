package ru.anykeyers.commonsapi.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
//    @JsonProperty(
//            access = JsonProperty.Access.READ_ONLY
//    )
    private UUID id;
    /**
     * Имя пользователя
     */
    private String username;
    /**
     * Пароль
     */
//    @JsonProperty(
//            access = JsonProperty.Access.WRITE_ONLY
//    )
    private String password;
    /**
     * Данные пользователя
     */
    private UserInfo userInfo;
    /**
     * Дата создания
     */
//    @JsonProperty(
//            access = JsonProperty.Access.READ_ONLY
//    )
    private long createdTimestamp;
}
