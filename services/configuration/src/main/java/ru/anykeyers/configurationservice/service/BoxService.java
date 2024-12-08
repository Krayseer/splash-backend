package ru.anykeyers.configurationservice.service;

import ru.anykeyers.commonsapi.domain.configuration.BoxDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.configurationservice.domain.Box;

import java.util.List;

/**
 * Сервис для обработки боксов
 */
public interface BoxService {

    /**
     * Получить бокс
     *
     * @param boxId идентификатор бокса
     */
    Box getBox(Long boxId);

    /**
     * Получить список боксов для автомойки
     *
     * @param carWashId идентификатор автоомойки
     */
    List<Box> getCarWashBoxes(Long carWashId);

    /**
     * Добавить бокс автомойке
     *
     * @param user      владелец автомойки
     * @param boxDTO    данные о боксе
     */
    void addBox(User user, BoxDTO boxDTO);

    /**
     * Обновить информацию о боксе
     */
    void updateBox(BoxDTO boxDTO);

    /**
     * Удалить бокс
     *
     * @param boxId идентификатор бокса
     */
    void deleteBox(Long boxId);

}
