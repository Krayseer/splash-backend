package ru.anykeyers.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.commonsapi.domain.order.OrderState;

import java.util.List;

/**
 * DAO для работы с заказами
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, PagingAndSortingRepository<Order, Long> {

    /**
     * Получить список заказов по статусу
     *
     * @param orderState    статус заказа
     */
    List<Order> findByState(OrderState orderState);

    /**
     * Получить список заказов пользователя по статусу
     *
     * @param username  имя пользователя
     * @param status    статус заказа
     */
    List<Order> findByUsernameAndState(String username, OrderState status);

    /**
     * Получить список заказов пользователя по списку статусов
     *
     * @param username      имя пользователя
     * @param orderStates   список состояний
     */
    List<Order> findByUsernameAndStateIn(String username, List<OrderState> orderStates);

    /**
     * Получить список заказов у автомойки
     *
     * @param carWashId     идентификатор автомойки
     * @param orderState    статус заказа
     */
    List<Order> findByCarWashIdAndState(Long carWashId, OrderState orderState);

    /**
     * Получить количество заказов у автомойки
     *
     * @param carWashId идентификатор автомойки
     * @param status    статус заказа
     */
    int countByCarWashIdAndState(Long carWashId, OrderState status);

    /**
     * Получить список заказов автомойки
     *
     * @param carWashId идентификатор автомойки
     */
    List<Order> findByCarWashId(Long carWashId);

    /**
     * Получить список заказов автомойки за промежуток времени
     *
     * @param carWashId идентификатор автомойки
     * @param states    статусы заказов
     * @param startTime начало отрезка
     * @param endTime   конец отрезка
     */
    @Query("SELECT o FROM Order o WHERE o.carWashId = :carWashId AND o.state IN :states AND (o.startTime = :startTime OR (o.startTime >= :startTime AND o.startTime <= :endTime))")
    List<Order> findCarWashOrdersByStatesAndInterval(
            @Param("carWashId") Long carWashId,
            @Param("states") List<OrderState> states,
            @Param("startTime") long startTime,
            @Param("endTime") long endTime
    );

}
