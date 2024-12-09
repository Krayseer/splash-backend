package ru.anykeyers.orderservice.web.controller;

/**
 * Название контроллера
 */
final class ControllerName {

    /**
     * Базовое название контроллеров
     */
    public static final String BASE_CONTROLLER = "/api/order";

    /**
     * Название контроллера для обработки заказов пользователей
     */
    public static final String USER_ORDER_CONTROLLER = BASE_CONTROLLER + "/user";

    /**
     * Название контроллера для обработки заказов автомоек
     */
    public static final String CAR_WASH_ORDER_CONTROLLER = BASE_CONTROLLER + "/car-wash";

}
