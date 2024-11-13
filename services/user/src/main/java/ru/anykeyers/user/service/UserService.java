package ru.anykeyers.user.service;

import org.springframework.web.multipart.MultipartFile;
import ru.anykeyers.commonsapi.domain.user.User;

import java.util.Set;
import java.util.UUID;

/**
 * Сервис обработки пользователей
 */
public interface UserService {

    /**
     * @return список всех пользователей
     */
    Set<User> getAllUsers();

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
     * Получить список пользователей
     *
     * @param ids идентификатор пользователей
     */
    Set<User> getUsers(Set<UUID> ids);

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
