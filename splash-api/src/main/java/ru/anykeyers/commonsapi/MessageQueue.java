package ru.anykeyers.commonsapi;

/**
 * Название очереди сообщений
 */
public final class MessageQueue {

    /**
     * Очередь с созданными заказами
     */
    public static final String ORDER_CREATE = "order-create";

    /**
     * Очередь с удаленными заказами
     */
    public static final String ORDER_DELETE = "order-delete";

    /**
     * Очередь одобрения приглашений
     */
    public static final String EMPLOYEE_INVITATION_APPLY = "invitation-apply";

    /**
     * Очередь назначения работника заказу
     */
    public static final String EMPLOYEE_ORDER_APPLY = "order-employee-apply";

    /**
     * Очередь удаления работника с заказа
     */
    public static final String ORDER_EMPLOYEE_DISAPPOINT = "order-employee-disappointment";

    /**
     * Очередь проверки ИНН автомоек
     */
    public static final String CAR_WASH_TIN_VALIDATE_RESULT = "car-wash-tin-check-result";

}
