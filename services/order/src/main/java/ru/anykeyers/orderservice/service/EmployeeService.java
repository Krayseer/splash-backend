package ru.anykeyers.orderservice.service;

public interface EmployeeService {

    /**
     * Обработать назначение работника заказу
     */
    void applyOrderEmployee(Long orderId);

    /**
     * Обработать удаление работника с заказа
     *
     * @param orderId идентификатор заказа
     */
    void disappointEmployeeFromOrder(long orderId);

}
