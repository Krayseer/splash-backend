package ru.anykeyers.user.service;

import org.springframework.web.multipart.MultipartFile;
import ru.anykeyers.commonsapi.domain.user.User;

import java.util.UUID;

/**
 * Сервис обработки пользователей
 */
public interface UserService {

    /**
     * Получить пользователя по id
     *
     * @param id идентификатор пользователя
     */
    User getUser(UUID id);

    /**
     * Получить пользователя
     *
     * @param username имя пользователя
     */
    User getUser(String username);

    /**
     * Добавить пользователя
     *
     * @param user пользователь
     */
    void addUser(User user);

    /**
     * Обновить пользователя
     *
     * @param user пользователь
     */
    void updateUser(User user);

    /**
     * Удалить пользователя
     *
     * @param id идентификатор пользователя
     */
    void deleteUser(UUID id);

    /**
     * Добавить фотографию пользователю
     *
     * @param user  пользователь
     * @param photo фотография
     */
    void addPhoto(User user, MultipartFile photo);

}
