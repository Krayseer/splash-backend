package ru.anykeyers.user.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Запрос на обновление данных пользователя
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    /**
     * Идентификатор пользователя
     */
    private UUID id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    private MultipartFile photo;

}
