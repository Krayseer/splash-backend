package ru.anykeyers.commonsapi.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Данные пользователя
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    /**
     * Имя
     */
    private String firstName;
    /**
     * Фамилия
     */
    private String lastName;
    /**
     * Почта
     */
    private String email;
    /**
     * Телефон
     */
    private String phoneNumber;
    /**
     * URL аватарки
     */
    private String photoUrl;
    /**
     * Настройки пользователя
     */
    private UserSettingDTO userSetting;
    /**
     * Список ролей
     */
    private List<String> roles;

    @JsonIgnore
    public String getFullName() {
        return firstName + " " + lastName;
    }

}
