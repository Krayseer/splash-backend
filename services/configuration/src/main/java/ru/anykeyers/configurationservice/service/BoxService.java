package ru.anykeyers.configurationservice.service;

import ru.anykeyers.commonsapi.domain.configuration.BoxDTO;
import ru.anykeyers.configurationservice.domain.Box;

import java.util.List;

/**
 * Сервис для обработки боксов
 */
public interface BoxService {

    /**
     * Получить список боксов для автомойки
     *
     * @param carWashId идентификатор автоомойки
     */
    List<Box> getCarWashBoxes(Long carWashId);

    /**
     * Добавить бокс автомойке
     */
    void addBox(BoxDTO boxDTO);

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
