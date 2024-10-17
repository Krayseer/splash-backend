package ru.anykeyers.statistics.service.batch.processor.order;

import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.statistics.service.batch.processor.BatchProcessor;

/**
 * Пакетная обработка заказов
 */
public interface OrderBatchProcessor extends BatchProcessor {

    /**
     * Получить задание на обработку заказа
     *
     * @param order заказ
     */
    Runnable getProcessTask(OrderDTO order);

}
